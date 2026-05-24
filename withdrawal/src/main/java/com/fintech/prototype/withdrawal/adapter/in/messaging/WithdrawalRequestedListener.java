package com.fintech.prototype.withdrawal.adapter.in.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.prototype.withdrawal.application.port.in.ProcessWithdrawalInboundPort;
import com.fintech.prototype.withdrawal.application.usecase.withdrawal.ProcessWithdrawalCommand;
import com.fintech.prototype.withdrawal.application.usecase.withdrawal.ProcessWithdrawalResult;
import com.fintech.prototype.withdrawal.dto.CashWithdrawalRequestDTO;
import com.fintech.prototype.withdrawal.dto.CashWithdrawalResponseDTO;
import com.fintech.prototype.withdrawal.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class WithdrawalRequestedListener {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final ProcessWithdrawalInboundPort useCase;
    private final String replyPrefix;

    public WithdrawalRequestedListener(RabbitTemplate rabbitTemplate,
                                       ObjectMapper objectMapper,
                                       ProcessWithdrawalInboundPort useCase,
                                       @Value("${fintech.messaging.withdrawal.reply-prefix}") String replyPrefix) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.useCase = useCase;
        this.replyPrefix = replyPrefix;
    }

    @RabbitListener(queues = "${fintech.messaging.withdrawal.queue}")
    public void processMessage(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String identifier = header(headers, "IDENTIFIER");
        String correlationId = header(headers, "Correlation-Id");
        String idempotencyKey = header(headers, "Idempotency-Key");

        try {
            CashWithdrawalRequestDTO request = objectMapper.readValue(message.getBody(), CashWithdrawalRequestDTO.class);
            ProcessWithdrawalResult result = useCase.process(new ProcessWithdrawalCommand(
                    identifier,
                    request.amount(),
                    request.password(),
                    correlationId,
                    idempotencyKey
            ));
            send(identifier, new CashWithdrawalResponseDTO(result.withdrawalId(), result.status()));
            log.info("Withdrawal processed. identifier={} correlationId={}", identifier, correlationId);
        } catch (Exception exception) {
            log.error("Withdrawal processing failed. identifier={} correlationId={}", identifier, correlationId, exception);
            send(identifier, new ErrorResponseDTO("WITHDRAWAL_ERROR", exception.getMessage(), correlationId));
        }
    }

    private void send(String identifier, Object response) {
        try {
            rabbitTemplate.convertAndSend(replyPrefix + "-" + identifier, "", objectMapper.writeValueAsString(response));
        } catch (Exception exception) {
            log.error("Could not send withdrawal response. identifier={}", identifier, exception);
        }
    }

    private String header(Map<String, Object> headers, String name) {
        Object value = headers.get(name);
        return value == null ? null : value.toString();
    }
}
