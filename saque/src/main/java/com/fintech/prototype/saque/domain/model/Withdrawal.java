package com.fintech.prototype.saque.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Withdrawal(
        String withdrawalId,
        String consultationId,
        BigDecimal amount,
        String status
) {
    public static Withdrawal approved(String consultationId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        return new Withdrawal(UUID.randomUUID().toString(), consultationId, amount, "APPROVED");
    }
}
