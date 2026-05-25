package com.longan.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.chat.entity.ChatConversation;
import com.longan.chat.mapper.ChatConversationMapper;
import com.longan.chat.service.ChatService;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatConversationMapper, ChatConversation> implements ChatService {
}
