package com.longan.JWT;

import com.longan.utils.UserContext; // 导入你的UserContext工具类
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtTokenInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenInterceptor.class);

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            // 当前拦截到的不是动态方法，直接放行
            return true;
        }

        // 1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getTokenName());

        // 2、处理Token前缀（前端传的是 Bearer xxx 格式）
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉"Bearer "前缀
        }

        // 3、校验令牌
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);

            // 核心修复：从Claims中提取用户ID，存入ThreadLocal
            Long userId = claims.get("id", Long.class);
            UserContext.setUserId(userId);
            // 4、通过，放行
            return true;
        } catch (Exception ex) {
            // 5、不通过，响应401状态码
            log.error("JWT校验失败", ex);
            response.setStatus(401);
            return false;
        }
    }

    // 核心修复：请求结束后清除ThreadLocal，防止内存泄漏
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}
