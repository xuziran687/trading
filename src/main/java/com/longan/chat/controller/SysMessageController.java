package com.longan.chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.longan.chat.entity.SysMessage;
import com.longan.chat.service.SysMessageService;
import com.longan.result.Result;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "系统消息")
@RestController
@RequestMapping("/api/chat/sys-messages")
@RequiredArgsConstructor
@Slf4j
public class SysMessageController {

    private final SysMessageService sysMessageService;

    @Operation(summary = "系统消息列表")
    @GetMapping
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        Long userId = UserContext.getUserId();
        Page<SysMessage> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<SysMessage> wrapper = new LambdaQueryWrapper<>(SysMessage.class)
                .eq(SysMessage::getUserId, userId)
                .orderByDesc(SysMessage::getCreateTime);
        IPage<SysMessage> result = Db.page(pageInfo, wrapper);
        return Result.success(result);
    }

    @Operation(summary = "未读消息数")
    @GetMapping("/unread-count")
    public Result unreadCount() {
        Long userId = UserContext.getUserId();
        long count = sysMessageService.lambdaQuery()
                .eq(SysMessage::getUserId, userId)
                .eq(SysMessage::getIsRead, 0)
                .count();
        return Result.success(count);
    }

    @Operation(summary = "标记已读")
    @PutMapping("/{id}/read")
    public Result markRead(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        SysMessage msg = sysMessageService.getById(id);
        if (msg == null || !msg.getUserId().equals(userId)) {
            return Result.error("消息不存在");
        }
        msg.setIsRead(1);
        sysMessageService.updateById(msg);
        return Result.success();
    }
}
