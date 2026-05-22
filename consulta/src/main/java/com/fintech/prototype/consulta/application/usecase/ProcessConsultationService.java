package com.fintech.prototype.consulta.application.usecase;

import com.fintech.prototype.consulta.application.port.in.ProcessConsultationInboundPort;
import com.fintech.prototype.consulta.application.port.out.RepositoryOutboundPort;
import com.fintech.prototype.consulta.application.usecase.consultation.ProcessConsultationCommand;
import com.fintech.prototype.consulta.application.usecase.consultation.ProcessConsultationResult;
import com.fintech.prototype.consulta.domain.model.ConsultationSession;
import org.springframework.stereotype.Service;

@Service
public class ProcessConsultationService implements ProcessConsultationInboundPort {

    private final RepositoryOutboundPort repository;

    public ProcessConsultationService(RepositoryOutboundPort repository) {
        this.repository = repository;
    }

    @Override
    public ProcessConsultationResult process(ProcessConsultationCommand command) {
        ConsultationSession session = ConsultationSession.approved(
                command.identifier(),
                command.agency(),
                command.account()
        );
        repository.save(session);
        return new ProcessConsultationResult(session.identifier(), session.document(), session.name(), "APPROVED");
    }
}
