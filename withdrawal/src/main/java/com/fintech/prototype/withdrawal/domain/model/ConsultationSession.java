package com.fintech.prototype.withdrawal.domain.model;

public record ConsultationSession(
        String identifier,
        String agency,
        String account,
        String document,
        String name
) {
}
