package com.fintech.prototype.consulta.dto;

public record ErrorResponseDTO(
        String error,
        String details,
        String correlationId
) {
}
