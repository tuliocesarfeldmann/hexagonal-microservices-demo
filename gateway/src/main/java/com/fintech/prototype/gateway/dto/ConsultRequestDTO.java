package com.fintech.prototype.gateway.dto;

import jakarta.validation.constraints.NotBlank;

public record ConsultRequestDTO(
        @NotBlank String identifier,
        @NotBlank String agency,
        @NotBlank String account
) {
}
