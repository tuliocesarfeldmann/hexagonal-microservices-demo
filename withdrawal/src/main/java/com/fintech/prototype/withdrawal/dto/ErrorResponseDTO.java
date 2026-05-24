package com.fintech.prototype.withdrawal.dto;

public record ErrorResponseDTO(
        String error,
        String details,
        String correlationId
) {
}
