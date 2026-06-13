package com.longan.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.chat.entity.ChatConversation;

public interface ChatConversationMapper extends BaseMapper<ChatConversation> {
    void updateUnreadCount(Long id, Integer unread);
}
