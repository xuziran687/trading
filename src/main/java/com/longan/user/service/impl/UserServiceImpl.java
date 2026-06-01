package com.longan.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.longan.JWT.JwtProperties;
import com.longan.JWT.JwtUtil;
import com.longan.user.dto.LoginDTO;
import com.longan.user.dto.UserInfoDTO;
import com.longan.user.entity.User;
import com.longan.user.entity.UserProfile;
import com.longan.user.mapper.UserMapper;
import com.longan.user.mapper.UserProfileMapper;
import com.longan.user.service.UserService;
import com.longan.user.vo.LoginVO;
import com.longan.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hp
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2026-02-05 13:43:12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileMapper userProfileMapper;
    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;


    @Override
    public LoginVO login(LoginDTO loginDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", loginDTO.getEmail());

        try {
            log.info("执行用户查询，条件：{}", queryWrapper.getSqlSegment());

            User user = userMapper.selectOne(queryWrapper);

            if (user == null) {
                log.info("邮箱不存在，邮箱：{}", loginDTO.getEmail());
                throw new RuntimeException("邮箱不存在");
            }

            if (!loginDTO.getPassword().equals(user.getPassword())) {
                log.info("密码错误，邮箱：{}", loginDTO.getEmail());
                throw new RuntimeException("密码错误");
            }

            log.info("用户登录成功，邮箱：{}", loginDTO.getEmail());

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            String token = JwtUtil.createJWT(
                    jwtProperties.getSecretKey(),
                    jwtProperties.getTtl(),
                    claims);

            return new LoginVO(
                    user.getId(),
                    user.getNickname(),
                    user.getAvatar(),
                    token);

        } catch (Exception e) {
            log.error("用户登录查询异常，邮箱：{}", loginDTO.getEmail(), e);
            throw new RuntimeException("用户查询失败", e);
        }
    }




    @Override
    public void insert(User user) {
        userMapper.insert(user);
    }

    @Override
    public User selectById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public void update(UserInfoDTO dto) {
        Long userId= UserContext.getUserId();
        User user = userMapper.selectById(userId);
        user.update(dto);
        userMapper.updateById(user);
    }
}




