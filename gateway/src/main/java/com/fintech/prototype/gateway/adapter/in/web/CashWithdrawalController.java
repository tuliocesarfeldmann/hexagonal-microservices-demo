package com.fintech.prototype.gateway.adapter.in.web;

import com.fintech.prototype.gateway.application.port.in.WithdrawalInboundPort;
import com.fintech.prototype.gateway.dto.CashWithdrawalRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CashWithdrawalController {

    private final WithdrawalInboundPort useCase;

    public CashWithdrawalController(WithdrawalInboundPort useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/{identifier}/cash-withdrawal")
    public ResponseEntity<?> transaction(@RequestBody @Valid CashWithdrawalRequestDTO cashWithdrawal,
                                         @PathVariable String identifier,
                                         @RequestHeader(value = "Correlation-Id", required = false) String correlationId,
                                         @RequestHeader("Idempotency-Key") String idempotencyKey) throws Exception {
        String effectiveCorrelationId = correlationId == null || correlationId.isBlank()
                ? UUID.randomUUID().toString()
                : correlationId;
        return ResponseEntity
                .ok()
                .header("Correlation-Id", effectiveCorrelationId)
                .body(useCase.request(cashWithdrawal, identifier, effectiveCorrelationId, idempotencyKey));
    }
}
