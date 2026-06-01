package com.longan.user.controller;

import com.longan.user.dto.*;
import com.longan.user.entity.UserBehavior;
import com.longan.user.service.*;
import com.longan.user.vo.LoginVO;
import com.longan.user.vo.WalletLogVO;
import com.longan.user.entity.User;
import com.longan.user.entity.UserCredit;
import com.longan.user.entity.UserProfile;
import com.longan.user.entity.UserWallet;
import com.longan.result.PageResult;
import com.longan.result.Result;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
//
//简单查询     → GET
//复杂查询     → POST（推荐🔥）
//新增         → POST
//更新         → PUT
//删除         → DELETE
public class UserController {
    private final UserService userService;
    private final UserWalletService userWalletService;
    private final UserProfileService userProfileService;
    private final UserCreditService userCreditService;
    private final UserBehaviorService userBehaviorService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        log.info("注册：{}", registerDTO);
        // 1. 创建用户主表
        User user = new User(registerDTO.getEmail(),registerDTO.getPassword());
        userService.insert(user); // 执行完后 user.getId() 自动回填
        // 2. 初始化钱包
        userWalletService.init(user.getId());
        // 3. 初始化信用
        userCreditService.init(user.getId());
        // 4. 初始化资料（空壳，用户后续填写）
        userProfileService.init(user.getId());

        return Result.success("注册成功");
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        log.info("登录：{}", loginDTO);
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result info() {
        Long userId = UserContext.getUserId();
        User user = userService.selectById(userId);
        return Result.success(user);
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/info")
    public Result updateInfo(@RequestBody UserInfoDTO dto) {
        userService.update(dto);
        return Result.success();
    }


    @Operation(summary=" 查询信用信息")
    @GetMapping("/credit")
    public Result getCredit() {
        Long userId = UserContext.getUserId();
        return Result.success(userCreditService.getUserCredit(userId));
    }

    @Operation(summary = "记录用户行为")
    @PostMapping("/behavior")
    public Result behavior(@RequestBody UserBehaviorDTO userBehaviorDTO) {
        userBehaviorService.insert(userBehaviorDTO);
        return Result.success();
    }


}
