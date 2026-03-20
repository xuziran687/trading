package com.longan.JWT;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtProperties {
    // 修正：小驼峰命名（符合JavaBean规范）
    private String secretKey = "longan-apex-jwt-secret-key-2026-very-very-secure!!!";
    private long ttl = 7200000; // 2小时
    // 建议改为Authorization（前端主流传参方式），如果要保留token也可以，后续前端要对应
    private String tokenName = "Authorization";
}
