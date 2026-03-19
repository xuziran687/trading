package com.longan.controller;

import com.longan.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "平台币交易")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Operation(summary = "购买商品", description = "扣除买家平台币余额并转入卖家账户")
    @PostMapping("/buy")
    public Result buy(@Parameter(description = "商品ID") @RequestParam Long id) {
        // 你的逻辑
        return Result.success();
    }
}
