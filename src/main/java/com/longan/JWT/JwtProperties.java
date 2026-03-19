package com.longan.JWT;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtProperties {
    private String SecretKey="longan-apex-jwt-secret-key-2026-very-very-secure!!!";
    private long Ttl=7200000;
    private String TokenName="token";

    public void setSecretKey(String secretKey) {
        SecretKey = secretKey;
    }
    public void setTtl(long ttl) {
        Ttl = ttl;
    }
    public void setTokenName(String tokenName) {
        TokenName = tokenName;
    }
    public String getSecretKey() {
        return SecretKey;
    }
    public long getTtl() {
        return Ttl;
    }
    public String getTokenName() {
        return TokenName;
    }
}
