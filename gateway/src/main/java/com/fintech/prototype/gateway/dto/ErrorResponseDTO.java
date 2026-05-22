package com.fintech.prototype.gateway.dto;

import java.time.Instant;

public record ErrorResponseDTO(
        String error,
        String details,
        String correlationId,
        Instant timestamp
) {
}
