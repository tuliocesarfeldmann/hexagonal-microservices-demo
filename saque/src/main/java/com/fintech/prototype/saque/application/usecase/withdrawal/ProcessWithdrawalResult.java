package com.fintech.prototype.saque.application.usecase.withdrawal;

public record ProcessWithdrawalResult(
        String withdrawalId,
        String status
) {
}
