package com.longan.chat.controller;

import com.longan.chat.dto.SendMessageDTO;
import com.longan.chat.service.ChatService;
import com.longan.chat.vo.ConversationVO;
import com.longan.chat.vo.MessageVO;
import com.longan.result.Result;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "聊天消息")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "会话列表")
    @GetMapping("/conversations")
    public Result<List<ConversationVO>> conversations() {
        Long userId = UserContext.getUserId();
        log.info("查询会话列表，userId: {}", userId);
        return Result.success(chatService.getConversations(userId));
    }

    @Operation(summary = "消息历史")
    @GetMapping("/messages")
    public Result<List<MessageVO>> messages(@RequestParam Long conversationId) {
        Long userId = UserContext.getUserId();
        log.info("查询消息历史，conversationId: {}, userId: {}", conversationId, userId);
        return Result.success(chatService.getMessages(conversationId, userId));
    }

    @Operation(summary = "发送消息")
    @PostMapping("/send")
    public Result<MessageVO> send(@RequestBody SendMessageDTO dto) {
        Long userId = UserContext.getUserId();
        log.info("发送消息，senderId: {}, dto: {}", userId, dto);
        MessageVO vo = chatService.sendMessage(dto, userId);
        return Result.success(vo);
    }

    @Operation(summary = "标记已读")
    @PutMapping("/messages/read")
    public Result markAsRead(@RequestParam Long conversationId) {
        Long userId = UserContext.getUserId();
        chatService.markAsRead(conversationId, userId);
        return Result.success();
    }
}
