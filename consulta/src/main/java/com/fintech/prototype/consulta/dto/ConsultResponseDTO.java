package com.fintech.prototype.consulta.dto;

public record ConsultResponseDTO(
        String consultationId,
        String document,
        String name,
        String status
) {
}
