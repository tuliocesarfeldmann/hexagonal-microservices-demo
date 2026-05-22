package com.fintech.prototype.gateway.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CashWithdrawalRequestDTO(
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull @Size(min = 4, max = 12) String password
) {
}
