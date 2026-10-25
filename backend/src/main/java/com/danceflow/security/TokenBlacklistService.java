package com.danceflow.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 登出 Token 黑名单：jti 写入 Redis，TTL 取 Token 剩余有效期，过期自动清理。
 * Redis 不可用时降级为放行（本地已清 Token，仅失去服务端吊销能力），保证演示可用。
 */
@Component
public class TokenBlacklistService {
    private static final Logger log = LoggerFactory.getLogger(TokenBlacklistService.class);
    private static final String KEY_PREFIX = "danceflow:auth:blacklist:";

    private final StringRedisTemplate redis;

    public TokenBlacklistService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void ban(String jti, Duration ttl) {
        if (jti == null || jti.isBlank() || ttl.isNegative() || ttl.isZero()) {
            return;
        }
        try {
            redis.opsForValue().set(KEY_PREFIX + jti, "1", ttl);
        } catch (Exception e) {
            log.warn("Token 黑名单写入失败，本次登出仅本地生效：{}", e.getMessage());
        }
    }

    public boolean banned(String jti) {
        if (jti == null || jti.isBlank()) {
            return false;
        }
        try {
            return Boolean.TRUE.equals(redis.hasKey(KEY_PREFIX + jti));
        } catch (Exception e) {
            log.warn("Token 黑名单查询失败，按未拉黑处理：{}", e.getMessage());
            return false;
        }
    }
}
