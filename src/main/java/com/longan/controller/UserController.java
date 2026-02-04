package com.longan.controller;

import com.longan.pojo.DTO.LoginDTO;
import com.longan.pojo.DTO.RegisterDTO;
import com.longan.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
//    1. 注册
//    POST /api/user/register
//    功能：手机号注册，自动创建钱包、信用记录
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO){

        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){

        return Result.success("登录成功");
    }

}
