package com.fintech.prototype.saque.domain.model;

public record ConsultationSession(
        String identifier,
        String agency,
        String account,
        String document,
        String name
) {
}
