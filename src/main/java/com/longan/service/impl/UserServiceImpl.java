package com.longan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.mapper.UserProfileMapper;
import com.longan.pojo.DTO.LoginDTO;
import com.longan.pojo.DTO.RegisterDTO;
import com.longan.service.UserService;
import com.longan.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author hp
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2026-02-05 13:43:12
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity>
    implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserProfileMapper userProfileMapper;


    @Override
    public UserEntity register(RegisterDTO dto) {

        // 1. 邮箱查重
        Long count = userMapper.selectCount(
                new QueryWrapper<UserEntity>()
                        .eq("email", dto.getEmail())
        );

        if (count > 0) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 2. 创建用户
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        userMapper.insert(user);
        return user;
    }


    @Override
    public UserEntity login(LoginDTO loginDTO) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginDTO.getEmail());
        if (userMapper.selectOne(queryWrapper) == null){
            log.info("用户不存在");
        }else{
            queryWrapper.eq("password", loginDTO.getPassword());
            if (userMapper.selectOne(queryWrapper) != null){
                return userMapper.selectOne(queryWrapper);
            }else{
                log.info("密码错误");
            }
        }
        return null;
    }

    @Override
    public UserProfileEntity getProfile(Long userId) {
        QueryWrapper<UserProfileEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        userProfileMapper.selectOne(queryWrapper);

        return null;
    }
}




