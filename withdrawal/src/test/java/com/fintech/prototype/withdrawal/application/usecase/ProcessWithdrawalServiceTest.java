package com.fintech.prototype.withdrawal.application.usecase;

import com.fintech.prototype.withdrawal.application.usecase.withdrawal.ProcessWithdrawalCommand;
import com.fintech.prototype.withdrawal.application.usecase.withdrawal.ProcessWithdrawalResult;
import com.fintech.prototype.withdrawal.domain.model.ConsultationSession;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProcessWithdrawalServiceTest {

    @Test
    void processApprovesWithdrawalWhenConsultationExists() {
        ProcessWithdrawalService service = new ProcessWithdrawalService(
                identifier -> Optional.of(new ConsultationSession(identifier, "0001", "12345-6", "03900000000", "Test")),
                key -> true
        );

        ProcessWithdrawalResult result = service.process(new ProcessWithdrawalCommand(
                "abc-123",
                BigDecimal.TEN,
                "1234",
                "correlation-1",
                "idem-1"
        ));

        assertThat(result.withdrawalId()).isNotBlank();
        assertThat(result.status()).isEqualTo("APPROVED");
    }

    @Test
    void processRejectsDuplicatedIdempotencyKey() {
        AtomicBoolean firstCall = new AtomicBoolean(true);
        ProcessWithdrawalService service = new ProcessWithdrawalService(
                identifier -> Optional.of(new ConsultationSession(identifier, "0001", "12345-6", "03900000000", "Test")),
                key -> firstCall.getAndSet(false)
        );

        ProcessWithdrawalCommand command = new ProcessWithdrawalCommand(
                "abc-123",
                BigDecimal.TEN,
                "1234",
                "correlation-1",
                "idem-1"
        );

        service.process(command);

        assertThatThrownBy(() -> service.process(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicated withdrawal request");
    }
}
