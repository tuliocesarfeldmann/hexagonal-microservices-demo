package com.fintech.prototype.consult.application.usecase.consultation;

public record ProcessConsultationCommand(
        String identifier,
        String agency,
        String account,
        String correlationId
) {
}
