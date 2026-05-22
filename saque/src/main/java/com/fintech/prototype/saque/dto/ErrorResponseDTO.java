package com.fintech.prototype.saque.dto;

public record ErrorResponseDTO(
        String error,
        String details,
        String correlationId
) {
}
