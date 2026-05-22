package com.fintech.prototype.gateway.dto;

public record ConsultResponseDTO(
        String consultationId,
        String name,
        String document,
        String status
) {
}
