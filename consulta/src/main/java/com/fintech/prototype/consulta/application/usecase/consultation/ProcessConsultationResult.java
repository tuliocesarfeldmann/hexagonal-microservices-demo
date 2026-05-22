package com.fintech.prototype.consulta.application.usecase.consultation;

public record ProcessConsultationResult(
        String consultationId,
        String document,
        String name,
        String status
) {
}
