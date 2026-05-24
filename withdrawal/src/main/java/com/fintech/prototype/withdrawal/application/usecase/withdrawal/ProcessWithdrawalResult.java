package com.fintech.prototype.withdrawal.application.usecase.withdrawal;

public record ProcessWithdrawalResult(
        String withdrawalId,
        String status
) {
}
