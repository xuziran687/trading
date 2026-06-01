    package com.longan.JWT;

    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.security.Keys;

    import javax.crypto.SecretKey;
    import java.nio.charset.StandardCharsets;
    import java.util.Date;
    import java.util.Map;

    public class JwtUtil {

        /**
         * 生成 JWT
         */
        public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {

            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            Date exp = new Date(nowMillis + ttlMillis);

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)      // 签发时间
                    .setExpiration(exp)   // 过期时间
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        }

        /**
         * 解析 JWT
         */
    public static Claims parseJWT(String secretKey, String token) {

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getBody();
    }
    }
