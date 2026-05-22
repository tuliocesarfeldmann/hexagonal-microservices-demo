package com.fintech.prototype.gateway.application.usecase;

import com.fintech.prototype.gateway.application.MessagingProperties;
import com.fintech.prototype.gateway.application.port.in.WithdrawalInboundPort;
import com.fintech.prototype.gateway.application.port.out.OperationDestination;
import com.fintech.prototype.gateway.application.port.out.OperationRequestReplyPort;
import com.fintech.prototype.gateway.dto.CashWithdrawalRequestDTO;
import com.fintech.prototype.gateway.dto.CashWithdrawalResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WithdrawalInboundService implements WithdrawalInboundPort {

    private final OperationRequestReplyPort requestReplyPort;
    private final MessagingProperties properties;

    public WithdrawalInboundService(OperationRequestReplyPort requestReplyPort, MessagingProperties properties) {
        this.requestReplyPort = requestReplyPort;
        this.properties = properties;
    }

    @Override
    public CashWithdrawalResponseDTO request(CashWithdrawalRequestDTO request,
                                             String identifier,
                                             String correlationId,
                                             String idempotencyKey) throws Exception {
        log.info("Requesting withdrawal. identifier={} correlationId={}", identifier, correlationId);
        return requestReplyPort.sendAndReceive(
                new OperationDestination(properties.withdrawal().exchange(), properties.withdrawal().replyPrefix()),
                request,
                identifier,
                correlationId,
                idempotencyKey,
                CashWithdrawalResponseDTO.class
        );
    }
}
