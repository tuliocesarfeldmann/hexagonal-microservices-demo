package com.fintech.prototype.gateway.application.usecase;

import com.fintech.prototype.gateway.application.MessagingProperties;
import com.fintech.prototype.gateway.application.port.in.ConsultationInboundPort;
import com.fintech.prototype.gateway.application.port.out.OperationDestination;
import com.fintech.prototype.gateway.application.port.out.OperationRequestReplyPort;
import com.fintech.prototype.gateway.dto.ConsultRequestDTO;
import com.fintech.prototype.gateway.dto.ConsultResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsultationInboundService implements ConsultationInboundPort {

    private final OperationRequestReplyPort requestReplyPort;
    private final MessagingProperties properties;

    public ConsultationInboundService(OperationRequestReplyPort requestReplyPort, MessagingProperties properties) {
        this.requestReplyPort = requestReplyPort;
        this.properties = properties;
    }

    @Override
    public ConsultResponseDTO request(ConsultRequestDTO request, String correlationId) throws Exception {
        log.info("Requesting consultation. identifier={} correlationId={}", request.identifier(), correlationId);
        return requestReplyPort.sendAndReceive(
                new OperationDestination(properties.consult().exchange(), properties.consult().replyPrefix()),
                request,
                request.identifier(),
                correlationId,
                null,
                ConsultResponseDTO.class
        );
    }
}
