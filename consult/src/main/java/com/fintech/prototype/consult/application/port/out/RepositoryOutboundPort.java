package com.fintech.prototype.consult.application.port.out;

import com.fintech.prototype.consult.domain.model.ConsultationSession;

public interface RepositoryOutboundPort {
    void save(ConsultationSession session);
}
