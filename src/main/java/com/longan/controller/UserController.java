package com.longan.controller;

import com.longan.JWT.JwtProperties;
import com.longan.JWT.JwtUtil;
import com.longan.pojo.DTO.LoginDTO;
import com.longan.pojo.DTO.RegisterDTO;
import com.longan.pojo.DTO.UserInfoDTO;
import com.longan.pojo.VO.LoginVO;
import com.longan.pojo.VO.RegisterVO;
import com.longan.result.Result;
import com.longan.service.UserService;
import com.longan.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
//    1. 注册
//    POST /api/user/register
//    功能：手机号注册，自动创建钱包、信用记录
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO){
        log.info("注册：{}", registerDTO);
        UserEntity userEntity =userService.register(registerDTO);
        RegisterVO registerVO = new RegisterVO(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getNickname(),
                userEntity.getEmail()
        );
        return Result.success(registerVO);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){
        log.info("登录：{}", loginDTO);
        UserEntity userEntity = userService.login(loginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userEntity.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims);
        LoginVO loginVO = new LoginVO(
                userEntity.getId(),
                userEntity.getNickname(),
                userEntity.getAvatar(),
                token
        );
        return Result.success(loginVO);
    }

    //获取当前用户信息
    //GET /api/user/info
    @GetMapping("/info")
    public Result info(){

        Long userId = UserContext.getUserId();

        UserEntity user = userService.getById(userId);

        return Result.success(user);
    }

    @PutMapping("/info")
    public Result updateInfo(@RequestBody UserInfoDTO dto){

        Long userId = UserContext.getUserId();

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());

        userService.updateById(user);

        return Result.success();
    }

    @GetMapping("/profile")
    public Result profile(){

        Long userId = UserContext.getUserId();

        UserEntity user = userService.getProfile(userId);

        return Result.success(user);
    }

    @PutMapping("/profile")
    public Result updateProfile(@RequestBody UserProfileDTO dto){

        Long userId = UserContext.getUserId();

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setNickname(dto.getNickname());
        user.setAvatar(dto.getAvatar());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());
        user.setIntroduction(dto.getIntroduction());

        userService.updateById(user);

        return Result.success();
    }







}
