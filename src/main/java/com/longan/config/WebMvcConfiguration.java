package com.longan.config;


import com.longan.JWT.JwtTokenInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
// 继承WebMvcConfigurer接口启动拦截器
public class WebMvcConfiguration implements WebMvcConfigurer{
    private static final Logger log = LoggerFactory.getLogger(WebMvcConfiguration.class);

    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;

    // 注册拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/api/**") // 拦截所有请求
                .excludePathPatterns(
                        "/api/user/login",
                        "/api/user/register"
                );
    }

}