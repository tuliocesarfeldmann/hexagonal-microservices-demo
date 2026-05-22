package com.fintech.prototype.consulta.application.port.out;

import com.fintech.prototype.consulta.domain.model.ConsultationSession;

public interface RepositoryOutboundPort {
    void save(ConsultationSession session);
}
