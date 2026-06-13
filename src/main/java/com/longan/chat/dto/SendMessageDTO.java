package com.longan.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "发送消息请求")
public class SendMessageDTO {

    @Schema(description = "会话ID（已有会话时传入）")
    private Long conversationId;

    @Schema(description = "接收方用户ID（新建会话时传入）")
    private Long receiverId;

    @Schema(description = "关联商品ID（新建会话时传入）")
    private Long goodsId;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "消息类型：1文字 2图片 3系统消息", example = "1")
    private Integer type = 1;
}
