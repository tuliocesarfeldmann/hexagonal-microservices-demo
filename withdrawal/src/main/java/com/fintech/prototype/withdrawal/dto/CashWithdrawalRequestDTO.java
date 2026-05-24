package com.fintech.prototype.withdrawal.dto;

import java.math.BigDecimal;

public record CashWithdrawalRequestDTO(
        BigDecimal amount,
        String password
) {
}
