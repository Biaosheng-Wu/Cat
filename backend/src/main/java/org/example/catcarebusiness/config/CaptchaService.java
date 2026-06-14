package org.example.catcarebusiness.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 * 优先使用 Redis 存储验证码，Redis 不可用时降级为内存存储
 */
@Service
public class CaptchaService {

    private static final long TTL_SECONDS = 300; // 5 分钟
    private static final String REDIS_PREFIX = "captcha:";

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    private final Map<String, CaptchaEntry> memoryStore = new ConcurrentHashMap<>();

    /** 生成验证码，返回 {captchaKey, captchaImage} */
    public Map<String, String> generate() {
        CaptchaUtils.CaptchaResult result = CaptchaUtils.generate();
        String captchaKey = UUID.randomUUID().toString().replace("-", "");

        store(captchaKey, result.getCode());

        return Map.of(
            "captchaKey", captchaKey,
            "captchaImage", result.getImage()
        );
    }

    /** 验证验证码，验证后立即销毁 */
    public boolean validate(String captchaKey, String captchaCode) {
        if (captchaKey == null || captchaCode == null) return false;

        String stored = getAndRemove(captchaKey);
        return stored != null && stored.equalsIgnoreCase(captchaCode.trim());
    }

    private void store(String key, String code) {
        if (redisTemplate != null) {
            try {
                redisTemplate.opsForValue().set(REDIS_PREFIX + key, code, TTL_SECONDS, TimeUnit.SECONDS);
                return;
            } catch (Exception e) { /* 降级内存 */ }
        }
        memoryStore.put(key, new CaptchaEntry(code, System.currentTimeMillis()));
    }

    private String getAndRemove(String key) {
        if (redisTemplate != null) {
            try {
                String code = redisTemplate.opsForValue().get(REDIS_PREFIX + key);
                if (code != null) {
                    redisTemplate.delete(REDIS_PREFIX + key);
                    return code;
                }
            } catch (Exception e) { /* 降级 */ }
        }

        CaptchaEntry entry = memoryStore.remove(key);
        if (entry == null) return null;
        if (System.currentTimeMillis() - entry.createdAt > TTL_SECONDS * 1000) return null;
        return entry.code;
    }

    private static class CaptchaEntry {
        final String code;
        final long createdAt;
        CaptchaEntry(String code, long createdAt) {
            this.code = code;
            this.createdAt = createdAt;
        }
    }
}
