package com.fintech.prototype.consulta.domain.model;

public record ConsultationSession(
        String identifier,
        String agency,
        String account,
        String document,
        String name
) {
    public static ConsultationSession approved(String identifier, String agency, String account) {
        return new ConsultationSession(identifier, agency, account, "03900000000", "Teste");
    }
}
