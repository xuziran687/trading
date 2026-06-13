package com.longan.chat.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ConversationVO {
    private Long id;
    private Long targetId;
    private String nickname;
    private String avatar;
    private Long goodsId;
    private String goodsTitle;
    private String lastMsg;
    private Integer unread;
    private Date updateTime;
}
