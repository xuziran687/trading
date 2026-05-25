package com.longan.user.controller;

import com.longan.JWT.JwtProperties;
import com.longan.JWT.JwtUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private final WalletLogService walletLogService;
    private final JwtProperties jwtProperties;
    private final UserProfileService userProfileService;
    private final UserCreditService userCreditService;
    private final UserBehaviorService userBehaviorService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        log.info("注册：{}", registerDTO);

        // 1. 创建用户主表
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        userService.insert(user); // 执行完后 user.getId() 自动回填

        // 2. 初始化钱包 (关键步骤)
        UserWallet wallet = new UserWallet();
        wallet.setUserId(user.getId());
        wallet.setBalance(BigDecimal.ZERO);      // 余额 0
        wallet.setFreeze(BigDecimal.ZERO);       // 冻结 0
        wallet.setTotalIncome(BigDecimal.ZERO);  // 累计收入 0
        wallet.setTotalOutcome(BigDecimal.ZERO); // 累计支出 0


        userWalletService.insert(wallet); // 调用对应的 mapper.insert

        // 3. 初始化信用
        UserCredit credit = new UserCredit();
        credit.setUserId(user.getId());
        credit.setScore(100);
        credit.setLevel(1);
        credit.setGoodNum(0);
        credit.setBadNum(0);
        credit.setCreateTime(new Date());
        credit.setUpdateTime(new Date());
        userCreditService.save(credit);

        // 4. 初始化资料（空壳，用户后续填写）
        UserProfile profile = new UserProfile();
        profile.setUserId(user.getId());
        profile.setGender(0);
        userProfileService.insert(profile);

        return Result.success();
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) throws Exception {
        log.info("登录：{}", loginDTO);

        // 1. 调用登录方法获取用户信息
        User user = userService.login(loginDTO);

        // 2. 关键：增加非空判断，避免空指针
        if (user == null) {
            // 返回登录失败的提示（前端可根据code/msg提示用户）
            return Result.error("用户名或密码错误");
        }

        // 3. 登录成功，生成jwt令牌（此时user一定非空）
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims);

        // 4. 组装返回结果
        LoginVO loginVO = new LoginVO(
                user.getId(),
                user.getNickname(),
                user.getAvatar(),
                token
        );

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

        Long userId = UserContext.getUserId();

        User user = new User();
        user.setId(userId);
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());
        userService.updateById(user);

        return Result.success();
    }
    //用户资料

    @Operation(summary = "设置用户资料")
    @PostMapping("/profile")
    public Result insertProfile(@RequestBody UserProfileDTO dto) {
        Long userId = UserContext.getUserId();

        UserProfile exist = userProfileService.getByUserId(userId);

        if (exist == null) {
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(userId);
            userProfile.setGender(dto.getGender());
            userProfile.setBirthday(dto.getBirthday());
            userProfile.setAddress(dto.getAddress());
            userProfile.setSignature(dto.getSignature());
            userProfileService.insert(userProfile);
        } else {
            userProfileService.updateByUserId(dto);
        }

        return Result.success();
    }

    @Operation(summary = "获取用户资料")
    @GetMapping("/profile")
    public Result profile() {
        Long userId = UserContext.getUserId();
        return Result.success(userProfileService.getByUserId(userId));
    }

    @Operation(summary = "更新用户资料")
    @PutMapping("/profile")
    public Result updateProfile(@RequestBody UserProfileDTO dto) {
        log.info("更新用户资料：{}", dto);
        userProfileService.updateByUserId(dto);

        return Result.success();
    }

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
