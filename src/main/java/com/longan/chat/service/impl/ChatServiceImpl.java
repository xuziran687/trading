package com.longan.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.chat.dto.SendMessageDTO;
import com.longan.chat.entity.ChatConversation;
import com.longan.chat.entity.ChatMessage;
import com.longan.chat.mapper.ChatConversationMapper;
import com.longan.chat.mapper.ChatMessageMapper;
import com.longan.chat.service.ChatService;
import com.longan.chat.vo.ConversationVO;
import com.longan.chat.vo.MessageVO;
import com.longan.goods.entity.Goods;
import com.longan.goods.service.GoodsService;
import com.longan.user.entity.User;
import com.longan.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl extends ServiceImpl<ChatConversationMapper, ChatConversation> implements ChatService {

    private final ChatMessageMapper chatMessageMapper;
    private final ChatConversationMapper chatConversationMapper;
    private final UserService userService;
    private final GoodsService goodsService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<ConversationVO> getConversations(Long userId) {
        List<ChatConversation> list = chatConversationMapper.selectList(
                new LambdaQueryWrapper<ChatConversation>()
                        .eq(ChatConversation::getUserId, userId)
                        .or()
                        .eq(ChatConversation::getTargetId, userId)
                        .orderByDesc(ChatConversation::getUpdateTime)
        );

        return list.stream().map(c -> {
            ConversationVO vo = new ConversationVO();
            vo.setId(c.getId());
            vo.setGoodsId(c.getGoodsId());
            vo.setLastMsg(c.getLastMsg());
            vo.setUnread(c.getUnread());
            vo.setUpdateTime(c.getUpdateTime());

            Long targetId = c.getUserId().equals(userId) ? c.getTargetId() : c.getUserId();
            vo.setTargetId(targetId);

            User user = userService.selectById(targetId);
            if (user != null) {
                vo.setNickname(user.getNickname());
                vo.setAvatar(user.getAvatar());
            }

            if (c.getGoodsId() != null) {
                Goods goods = goodsService.selectById(c.getGoodsId());
                if (goods != null) {
                    vo.setGoodsTitle(goods.getTitle());
                }
            }

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MessageVO> getMessages(Long conversationId, Long userId) {
        ChatConversation conversation = chatConversationMapper.selectById(conversationId);
        if (conversation == null) {
            throw new RuntimeException("会话不存在");
        }
        if (!conversation.getUserId().equals(userId) && !conversation.getTargetId().equals(userId)) {
            throw new RuntimeException("无权查看该会话");
        }

        Long userA = conversation.getUserId();
        Long userB = conversation.getTargetId();

        List<ChatMessage> messages = chatMessageMapper.selectList(
                new LambdaQueryWrapper<ChatMessage>()
                        .and(w -> w.eq(ChatMessage::getSenderId, userA)
                                .eq(ChatMessage::getReceiverId, userB))
                        .or(w -> w.eq(ChatMessage::getSenderId, userB)
                                .eq(ChatMessage::getReceiverId, userA))
                        .orderByAsc(ChatMessage::getCreateTime)
        );

        return messages.stream().map(this::toMessageVO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageVO sendMessage(SendMessageDTO dto, Long senderId) {
        Long conversationId = dto.getConversationId();
        Long receiverId;
        Long goodsId = dto.getGoodsId();

        if (conversationId != null) {
            ChatConversation conversation = chatConversationMapper.selectById(conversationId);
            if (conversation == null) {
                throw new RuntimeException("会话不存在");
            }
            receiverId = conversation.getUserId().equals(senderId)
                    ? conversation.getTargetId()
                    : conversation.getUserId();
            goodsId = conversation.getGoodsId();
        } else {
            if (dto.getReceiverId() == null) {
                throw new RuntimeException("请指定接收方");
            }
            receiverId = dto.getReceiverId();

            ChatConversation existing = chatConversationMapper.selectOne(
                    new LambdaQueryWrapper<ChatConversation>()
                            .and(w -> w.eq(ChatConversation::getUserId, senderId)
                                    .eq(ChatConversation::getTargetId, receiverId))
                            .or(w -> w.eq(ChatConversation::getUserId, receiverId)
                                    .eq(ChatConversation::getTargetId, senderId))
            );
            if (existing != null) {
                conversationId = existing.getId();
            } else {
                ChatConversation conversation = new ChatConversation();
                conversation.setUserId(senderId);
                conversation.setTargetId(receiverId);
                conversation.setGoodsId(goodsId);
                conversation.setUnread(0);
                chatConversationMapper.insert(conversation);
                conversationId = conversation.getId();
            }
        }

        ChatMessage msg = new ChatMessage();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setGoodsId(goodsId);
        msg.setContent(dto.getContent());
        msg.setType(Objects.requireNonNullElse(dto.getType(), 1));
        msg.setStatus(0);
        chatMessageMapper.insert(msg);

        ChatConversation conversation = chatConversationMapper.selectById(conversationId);
        conversation.setLastMsg(dto.getContent());
        if (!conversation.getUserId().equals(senderId)) {
            conversation.setUnread(conversation.getUnread() + 1);
        } else if (!conversation.getTargetId().equals(senderId)) {
            conversation.setUnread(conversation.getUnread() + 1);
        }
        chatConversationMapper.updateById(conversation);

        MessageVO vo = toMessageVO(msg);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(receiverId),
                "/topic/messages",
                vo
        );

        log.info("消息发送成功: senderId={}, receiverId={}, conversationId={}", senderId, receiverId, conversationId);
        return vo;
    }

    @Override
    @Transactional
    public void markAsRead(Long conversationId, Long userId) {
        ChatConversation conversation = chatConversationMapper.selectById(conversationId);
        if (conversation == null) return;

        Long targetId = conversation.getUserId().equals(userId) ? conversation.getTargetId() : conversation.getUserId();
        chatMessageMapper.updateReadStatus(userId, targetId, conversation.getGoodsId());

        chatConversationMapper.updateUnreadCount(conversationId, 0);
    }

    private MessageVO toMessageVO(ChatMessage msg) {
        MessageVO vo = new MessageVO();
        vo.setId(msg.getId());
        vo.setSenderId(msg.getSenderId());
        vo.setReceiverId(msg.getReceiverId());
        vo.setGoodsId(msg.getGoodsId());
        vo.setContent(msg.getContent());
        vo.setType(msg.getType());
        vo.setStatus(msg.getStatus());
        vo.setCreateTime(msg.getCreateTime());

        User sender = userService.selectById(msg.getSenderId());
        if (sender != null) {
            vo.setSenderNickname(sender.getNickname());
            vo.setSenderAvatar(sender.getAvatar());
        }
        return vo;
    }
}
