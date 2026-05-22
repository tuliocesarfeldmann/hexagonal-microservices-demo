package com.fintech.prototype.saque.application.usecase.withdrawal;

import java.math.BigDecimal;

public record ProcessWithdrawalCommand(
        String identifier,
        BigDecimal amount,
        String password,
        String correlationId,
        String idempotencyKey
) {
}
