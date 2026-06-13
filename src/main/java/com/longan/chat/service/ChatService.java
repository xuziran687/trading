package com.longan.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longan.chat.dto.SendMessageDTO;
import com.longan.chat.entity.ChatConversation;
import com.longan.chat.vo.ConversationVO;
import com.longan.chat.vo.MessageVO;

import java.util.List;

public interface ChatService extends IService<ChatConversation> {

    List<ConversationVO> getConversations(Long userId);

    List<MessageVO> getMessages(Long conversationId, Long userId);

    MessageVO sendMessage(SendMessageDTO dto, Long senderId);

    void markAsRead(Long conversationId, Long userId);
}
