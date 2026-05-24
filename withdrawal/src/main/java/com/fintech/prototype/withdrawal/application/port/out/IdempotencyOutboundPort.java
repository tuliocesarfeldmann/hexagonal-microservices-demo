package com.fintech.prototype.withdrawal.application.port.out;

public interface IdempotencyOutboundPort {
    boolean reserve(String key);
}
