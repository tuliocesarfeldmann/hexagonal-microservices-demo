package com.fintech.prototype.gateway.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fintech.messaging")
public record MessagingProperties(
        Operation consult,
        Operation withdrawal,
        long replyTtl
) {
    public record Operation(String exchange, String replyPrefix) {
    }
}
