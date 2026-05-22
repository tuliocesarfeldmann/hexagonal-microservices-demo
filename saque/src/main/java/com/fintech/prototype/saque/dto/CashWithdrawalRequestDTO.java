package com.fintech.prototype.saque.dto;

import java.math.BigDecimal;

public record CashWithdrawalRequestDTO(
        BigDecimal amount,
        String password
) {
}
