package com.longan.JWT;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtProperties {
    private String SecretKey;
    private long Ttl;
    private String TokenName;
}
