package com.longan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longan.mapper.UserProfileMapper;
import com.longan.pojo.DTO.LoginDTO;
import com.longan.pojo.DTO.RegisterDTO;
import com.longan.pojo.entity.User;
import com.longan.pojo.entity.UserProfile;
import com.longan.service.UserService;
import com.longan.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final UserProfileMapper userProfileMapper;


    @Override
    public User register(RegisterDTO dto) {

        // 1. 邮箱查重
        Long count = baseMapper.selectCount(
                new QueryWrapper<User>()
                        .eq("email", dto.getEmail())
        );

        if (count > 0) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 2. 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        baseMapper.insert(user);
        return user;
    }


    @Override
    public User login(LoginDTO loginDTO){
        // 1. 初始化查询条件（仅查用户名）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", loginDTO.getEmail());

        try {
            log.info("执行用户查询，条件：{}", queryWrapper.getSqlSegment());

            // 2. 只查询一次数据库，获取用户信息
            User user = baseMapper.selectOne(queryWrapper);

            // 3. 第一步：判断邮箱是否存在
            if (user == null) {
                log.info("邮箱不存在，用户名：{}", loginDTO.getEmail());
                return null; // 直接返回null，告知Controller登录失败
            }

            // 4. 第二步：对比密码（关键！直接用已查询的用户对象对比，无需重复查库）
            // 【重要】生产环境必须用加密密码对比（比如BCrypt），这里先写明文示例，后续要替换
            if (!loginDTO.getPassword().equals(user.getPassword())) {
                log.info("密码错误，用户名：{}", loginDTO.getEmail());
                return null; // 密码错误也返回null
            }

            // 5. 登录成功：返回完整的用户对象（仅返回必要字段更佳）
            log.info("用户登录成功，用户名：{}", loginDTO.getEmail());
            return user;

        } catch (Exception e) {
            log.error("用户登录查询异常，用户名：{}", loginDTO.getEmail(), e);
            // 若没有自定义异常，也可抛运行时异常，但要确保外层Controller能捕获
            throw new RuntimeException("用户查询失败", e);
        }
    }


    @Override
    public UserProfile getProfile(Long userId) {
        QueryWrapper<UserProfile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        userProfileMapper.selectOne(queryWrapper);

        return null;
    }
}




