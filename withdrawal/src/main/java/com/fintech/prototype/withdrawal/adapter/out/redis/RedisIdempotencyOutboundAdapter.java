package com.fintech.prototype.withdrawal.adapter.out.redis;

import com.fintech.prototype.withdrawal.application.port.out.IdempotencyOutboundPort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisIdempotencyOutboundAdapter implements IdempotencyOutboundPort {

    private final StringRedisTemplate redisTemplate;

    public RedisIdempotencyOutboundAdapter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean reserve(String key) {
        Boolean reserved = redisTemplate.opsForValue()
                .setIfAbsent("idempotency:withdrawal:" + key, "PROCESSING", Duration.ofMinutes(10));
        return Boolean.TRUE.equals(reserved);
    }
}
