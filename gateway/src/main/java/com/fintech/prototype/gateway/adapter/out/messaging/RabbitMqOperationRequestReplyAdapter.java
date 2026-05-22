package com.fintech.prototype.gateway.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.prototype.gateway.application.MessagingProperties;
import com.fintech.prototype.gateway.application.port.out.OperationDestination;
import com.fintech.prototype.gateway.application.port.out.OperationRequestReplyPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitMqOperationRequestReplyAdapter implements OperationRequestReplyPort {

    private static final String CORRELATION_ID = "Correlation-Id";
    private static final String IDEMPOTENCY_KEY = "Idempotency-Key";
    private static final String IDENTIFIER = "IDENTIFIER";

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final MessagingProperties properties;

    public RabbitMqOperationRequestReplyAdapter(RabbitTemplate rabbitTemplate,
                                                ObjectMapper objectMapper,
                                                MessagingProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    @Override
    public <T> T sendAndReceive(OperationDestination destination,
                                Object payload,
                                String identifier,
                                String correlationId,
                                String idempotencyKey,
                                Class<T> responseType) throws Exception {

        String responseQueue = declareResponseQueue(destination.replyPrefix(), identifier);

        Map<String, Object> headers = new HashMap<>();
        headers.put(IDENTIFIER, identifier);
        headers.put(CORRELATION_ID, correlationId);

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            headers.put(IDEMPOTENCY_KEY, idempotencyKey);
        }

        rabbitTemplate.convertAndSend(destination.exchange(), "", objectMapper.writeValueAsString(payload), message -> {
            message.getMessageProperties().getHeaders().putAll(headers);
            message.getMessageProperties().setCorrelationId(correlationId);
            return message;
        });

        Object response = rabbitTemplate.receiveAndConvert(responseQueue, properties.replyTtl());
        if (response == null) {
            throw new IllegalStateException("Timeout waiting for response on queue " + responseQueue);
        }

        String json = response.toString();

        if (json.contains("\"error\"")) {
            throw new IllegalStateException(json);
        }

        return objectMapper.readValue(json, responseType);
    }

    private String declareResponseQueue(String replyPrefix, String identifier) {

        String responseQueue = replyPrefix + "-" + identifier;

        rabbitTemplate.execute(channel -> {
            Map<String, Object> args = new HashMap<>();
            args.put("x-message-ttl", properties.replyTtl());
            args.put("x-expires", properties.replyTtl());
            channel.queueDeclare(responseQueue, false, true, true, args);
            channel.exchangeDeclare(responseQueue, "direct", false, true, null);
            channel.queueBind(responseQueue, responseQueue, "");
            return null;
        });

        return responseQueue;
    }
}
