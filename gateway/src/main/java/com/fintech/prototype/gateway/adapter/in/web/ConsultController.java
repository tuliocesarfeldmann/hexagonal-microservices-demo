package com.fintech.prototype.gateway.adapter.in.web;

import com.fintech.prototype.gateway.application.port.in.ConsultationInboundPort;
import com.fintech.prototype.gateway.dto.ConsultRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ConsultController {

    private final ConsultationInboundPort useCase;

    public ConsultController(ConsultationInboundPort useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/consult")
    public ResponseEntity<?> consult(@RequestBody @Valid ConsultRequestDTO consult,
                                     @RequestHeader(value = "Correlation-Id", required = false) String correlationId) throws Exception {
        String effectiveCorrelationId = correlationId == null || correlationId.isBlank()
                ? UUID.randomUUID().toString()
                : correlationId;
        return ResponseEntity
                .ok()
                .header("Correlation-Id", effectiveCorrelationId)
                .body(useCase.request(consult, effectiveCorrelationId));
    }
}
