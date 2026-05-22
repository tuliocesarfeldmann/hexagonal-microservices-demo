package com.fintech.prototype.gateway.application.port.out;

public record OperationDestination(
        String exchange,
        String replyPrefix
) {
}
