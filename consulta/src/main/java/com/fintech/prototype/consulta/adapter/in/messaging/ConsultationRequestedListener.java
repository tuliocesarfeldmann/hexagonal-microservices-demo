package com.fintech.prototype.consulta.adapter.in.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.prototype.consulta.application.port.in.ProcessConsultationInboundPort;
import com.fintech.prototype.consulta.application.usecase.consultation.ProcessConsultationCommand;
import com.fintech.prototype.consulta.application.usecase.consultation.ProcessConsultationResult;
import com.fintech.prototype.consulta.dto.ConsultRequestDTO;
import com.fintech.prototype.consulta.dto.ConsultResponseDTO;
import com.fintech.prototype.consulta.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ConsultationRequestedListener {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final ProcessConsultationInboundPort useCase;
    private final String replyPrefix;

    public ConsultationRequestedListener(RabbitTemplate rabbitTemplate,
                                         ObjectMapper objectMapper,
                                         ProcessConsultationInboundPort useCase,
                                         @Value("${fintech.messaging.consult.reply-prefix}") String replyPrefix) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.useCase = useCase;
        this.replyPrefix = replyPrefix;
    }

    @RabbitListener(queues = "${fintech.messaging.consult.queue}")
    public void processMessage(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String identifier = header(headers, "IDENTIFIER");
        String correlationId = header(headers, "Correlation-Id");

        try {
            ConsultRequestDTO request = objectMapper.readValue(message.getBody(), ConsultRequestDTO.class);
            ProcessConsultationResult result = useCase.process(new ProcessConsultationCommand(
                    identifier,
                    request.agency(),
                    request.account(),
                    correlationId
            ));
            send(identifier, new ConsultResponseDTO(
                    result.consultationId(),
                    result.document(),
                    result.name(),
                    result.status()
            ));
            log.info("Consultation processed. identifier={} correlationId={}", identifier, correlationId);
        } catch (Exception exception) {
            log.error("Consultation processing failed. identifier={} correlationId={}", identifier, correlationId, exception);
            send(identifier, new ErrorResponseDTO("INTERNAL_ERROR", exception.getMessage(), correlationId));
        }
    }

    private void send(String identifier, Object response) {
        try {
            rabbitTemplate.convertAndSend(replyPrefix + "-" + identifier, "", objectMapper.writeValueAsString(response));
        } catch (Exception exception) {
            log.error("Could not send consultation response. identifier={}", identifier, exception);
        }
    }

    private String header(Map<String, Object> headers, String name) {
        Object value = headers.get(name);
        return value == null ? null : value.toString();
    }
}
