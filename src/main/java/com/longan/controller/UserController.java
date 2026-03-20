package com.longan.controller;

import com.longan.JWT.JwtProperties;
import com.longan.JWT.JwtUtil;
import com.longan.pojo.DTO.LoginDTO;
import com.longan.pojo.DTO.RegisterDTO;
import com.longan.pojo.DTO.UserInfoDTO;
import com.longan.pojo.DTO.UserProfileDTO;
import com.longan.pojo.VO.LoginVO;
import com.longan.pojo.VO.RegisterVO;
import com.longan.pojo.entity.User;
import com.longan.result.Result;
import com.longan.service.UserService;
import com.longan.utils.UserContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtProperties jwtProperties;
//    1. 注册
//    POST /api/user/register
//    功能：手机号注册，自动创建钱包、信用记录
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO){
        log.info("注册：{}", registerDTO);
        User userEntity =userService.register(registerDTO);
        RegisterVO registerVO = new RegisterVO(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getNickname(),
                userEntity.getEmail()
        );
        return Result.success(registerVO);
    }

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

        // 3. 登录成功，生成jwt令牌（此时userEntity一定非空）
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


    //获取当前用户信息
    //GET /api/user/info
    @GetMapping("/info")
    public Result info(){

        Long userId = UserContext.getUserId();

        User user = userService.getById(userId);

        return Result.success(user);
    }

    @PutMapping("/info")
    public Result updateInfo(@RequestBody UserInfoDTO dto){

        Long userId = UserContext.getUserId();

        User user = new User();
        user.setId(userId);
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());

        userService.updateById(user);

        return Result.success();
    }

    @GetMapping("/profile")
    public Result profile(){

        Long userId = UserContext.getUserId();
        Result<UserProfileDTO> result = Result.success();
        return result.success(userService.getProfile(userId));
        
    }

    @PutMapping("/profile")
    public Result updateProfile(@RequestBody UserProfileDTO dto){

        Long userId = UserContext.getUserId();

        User user = new User();
        user.setId(userId);
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());

        userService.updateById(user);

        return Result.success();
    }







}
