package com.longan.user.controller;

import com.longan.result.PageResult;
import com.longan.result.Result;
import com.longan.user.service.UserWalletService;
import com.longan.user.service.WalletLogService;
import com.longan.user.vo.WalletLogVO;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户钱包接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserWalletController {
    private final UserWalletService userWalletService;
    private final WalletLogService walletLogService;
    @Operation(summary = "查询当前用户钱包")
    @GetMapping("/wallet")
    public Result getWallet() {
        Long userId = UserContext.getUserId();
        return Result.success(userWalletService.getWallet(userId));

    }

    @Operation(summary="查询钱包流水")
    @GetMapping("/wallet/log")
    public Result getWalletLog(
            @Parameter(description = "类型：1-收入 2-支出") Integer type,
            @Parameter(description = "业务类型：1-充值 2-消费 3-退款 4-佣金 5-签到 6-活动") Integer businessType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = UserContext.getUserId();
        PageResult<WalletLogVO> pageResult = walletLogService.getWalletLog(userId, type, businessType, page, size);
        return Result.success(pageResult);
    }
}
