package com.fintech.prototype.saque.application.port.out;

public interface IdempotencyOutboundPort {
    boolean reserve(String key);
}
