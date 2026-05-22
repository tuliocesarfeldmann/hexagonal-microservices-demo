package com.fintech.prototype.consulta.application.usecase.consultation;

public record ProcessConsultationCommand(
        String identifier,
        String agency,
        String account,
        String correlationId
) {
}
