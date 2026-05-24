package com.fintech.prototype.consult.dto;

public record ConsultResponseDTO(
        String consultationId,
        String document,
        String name,
        String status
) {
}
