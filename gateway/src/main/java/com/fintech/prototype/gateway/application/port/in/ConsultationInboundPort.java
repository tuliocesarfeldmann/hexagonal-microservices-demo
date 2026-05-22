package com.fintech.prototype.gateway.application.port.in;

import com.fintech.prototype.gateway.dto.ConsultRequestDTO;
import com.fintech.prototype.gateway.dto.ConsultResponseDTO;

public interface ConsultationInboundPort {
    ConsultResponseDTO request(ConsultRequestDTO request, String correlationId) throws Exception;
}
