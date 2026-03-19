package com.longan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("闲鱼二手平台 - 内部接口文档")
                        .version("1.0.0")
                        .description("包含主页、发布、消息、我的四大模块及平台币支付逻辑")
                        .contact(new Contact().name("全栈开发组").email("admin@idle.com")));
    }

    @Bean
    public GroupedOpenApi goodsApi() {
        return GroupedOpenApi.builder()
                .group("01-商品发布与管理模块")
                .pathsToMatch("/api/goods/**", "/api/category/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("02-用户与消息模块")
                .pathsToMatch("/api/user/**", "/api/chat/**")
                .build();
    }
}
