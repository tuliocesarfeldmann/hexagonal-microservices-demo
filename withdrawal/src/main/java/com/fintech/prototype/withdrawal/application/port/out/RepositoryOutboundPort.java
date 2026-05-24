package com.fintech.prototype.withdrawal.application.port.out;

import com.fintech.prototype.withdrawal.domain.model.ConsultationSession;

import java.util.Optional;

public interface RepositoryOutboundPort {
    Optional<ConsultationSession> findById(String identifier);
}
