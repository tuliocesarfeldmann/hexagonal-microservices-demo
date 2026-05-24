package com.fintech.prototype.consult.dto;

public record ErrorResponseDTO(
        String error,
        String details,
        String correlationId
) {
}
