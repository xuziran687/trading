package com.longan.user.controller;

import com.longan.result.Result;
import com.longan.user.dto.UserProfileDTO;
import com.longan.user.entity.UserProfile;
import com.longan.user.service.UserProfileService;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户资料详情接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Operation(summary = "获取用户资料")
    @GetMapping("/profile")
    public Result profile() {
        log.info("获取用户资料");
        Long userId = UserContext.getUserId();
        return Result.success(userProfileService.getByUserId(userId));
    }

    @Operation(summary = "更新用户资料")
    @PutMapping("/profile")
    public Result updateProfile(@RequestBody UserProfileDTO dto) {
        log.info("更新用户资料：{}", dto);
        userProfileService.update(dto);
        return Result.success();
    }
}
