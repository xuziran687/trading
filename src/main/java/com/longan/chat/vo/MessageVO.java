package com.longan.chat.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MessageVO {
    private Long id;
    private Long senderId;
    private String senderNickname;
    private String senderAvatar;
    private Long receiverId;
    private Long goodsId;
    private String content;
    private Integer type;
    private Integer status;
    private Date createTime;
}
