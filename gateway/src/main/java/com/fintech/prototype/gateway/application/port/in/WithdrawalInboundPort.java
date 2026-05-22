package com.fintech.prototype.gateway.application.port.in;

import com.fintech.prototype.gateway.dto.CashWithdrawalRequestDTO;
import com.fintech.prototype.gateway.dto.CashWithdrawalResponseDTO;

public interface WithdrawalInboundPort {
    CashWithdrawalResponseDTO request(CashWithdrawalRequestDTO request,
                                      String identifier,
                                      String correlationId,
                                      String idempotencyKey) throws Exception;
}
