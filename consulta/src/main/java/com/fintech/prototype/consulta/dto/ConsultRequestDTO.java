package com.fintech.prototype.consulta.dto;

public record ConsultRequestDTO(
        String identifier,
        String agency,
        String account
) {
}
