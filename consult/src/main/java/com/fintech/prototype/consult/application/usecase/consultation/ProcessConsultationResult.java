package com.fintech.prototype.consult.application.usecase.consultation;

public record ProcessConsultationResult(
        String consultationId,
        String document,
        String name,
        String status
) {
}
