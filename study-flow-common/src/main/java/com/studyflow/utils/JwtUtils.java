package com.studyflow.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类
 *
 * 登录成功后生成 token，后续请求通过 Authorization 头传递。
 * token 中包含了 userId，拦截器解析后存入 UserHolder。
 */
public class JwtUtils {

    // 密钥 - 生产环境应从配置文件或环境变量读取
    private static final String SECRET = System.getenv().getOrDefault("JWT_SECRET", "StudyFlow2026SecretKeyForJwtToken!@#$%^");

    // 过期时间：7天（毫秒）
    private static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000L;

    private static final SecretKey KEY = Keys.hmacShaKeyFor(
            SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成 JWT token
     * @param userId 用户ID
     * @return token 字符串
     */
    public static String generateToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    /**
     * 从 token 中解析 userId
     * @param token JWT token
     * @return userId，解析失败返回 null
     */
    public static Long parseUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            return null;
        }
    }
}
