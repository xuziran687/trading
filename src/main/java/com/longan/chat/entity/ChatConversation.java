package com.longan.chat.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 聊天会话表
 * @TableName chat_conversation
 */
@TableName(value ="chat_conversation")
@Data
public class ChatConversation implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 对方ID
     */
    private Long targetId;

    /**
     * 关联商品ID
     */
    private Long goodsId;

    /**
     * 最后一条消息
     */
    private String lastMsg;

    /**
     * 未读消息数
     */
    private Integer unread;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}