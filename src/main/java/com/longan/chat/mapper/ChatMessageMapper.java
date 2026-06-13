package com.longan.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longan.chat.entity.ChatMessage;

public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    void updateReadStatus(Long userId, Long targetId, Long goodsId);
}
