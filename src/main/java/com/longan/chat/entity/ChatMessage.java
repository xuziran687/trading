package com.longan.chat.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 聊天消息表
 * @TableName chat_message
 */
@TableName(value ="chat_message")
@Data
public class ChatMessage implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 接收者ID
     */
    private Long receiverId;

    /**
     * 关联商品ID
     */
    private Long goodsId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 1文字 2图片 3系统消息
     */
    private Integer type;

    /**
     * 0未读 1已读
     */
    private Integer status;

    /**
     * 发送时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}