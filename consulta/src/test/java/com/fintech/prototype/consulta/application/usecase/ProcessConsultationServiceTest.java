package com.fintech.prototype.consulta.application.usecase;

import com.fintech.prototype.consulta.application.usecase.consultation.ProcessConsultationCommand;
import com.fintech.prototype.consulta.application.usecase.consultation.ProcessConsultationResult;
import com.fintech.prototype.consulta.domain.model.ConsultationSession;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessConsultationServiceTest {

    @Test
    void processCreatesApprovedConsultationAndStoresSession() {
        AtomicReference<ConsultationSession> savedSession = new AtomicReference<>();
        ProcessConsultationService service = new ProcessConsultationService(savedSession::set);

        ProcessConsultationResult result = service.process(new ProcessConsultationCommand(
                "abc-123",
                "0001",
                "12345-6",
                "correlation-1"
        ));

        assertThat(result.status()).isEqualTo("APPROVED");
        assertThat(result.consultationId()).isEqualTo("abc-123");
        assertThat(savedSession.get().account()).isEqualTo("12345-6");
    }
}
