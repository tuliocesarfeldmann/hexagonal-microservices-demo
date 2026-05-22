package com.fintech.prototype.gateway.application.port.out;

public interface OperationRequestReplyPort {
    <T> T sendAndReceive(OperationDestination destination,
                         Object payload,
                         String identifier,
                         String correlationId,
                         String idempotencyKey,
                         Class<T> responseType) throws Exception;
}
