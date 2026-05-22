package com.fintech.prototype.saque.application.port.out;

import com.fintech.prototype.saque.domain.model.ConsultationSession;

import java.util.Optional;

public interface RepositoryOutboundPort {
    Optional<ConsultationSession> findById(String identifier);
}
