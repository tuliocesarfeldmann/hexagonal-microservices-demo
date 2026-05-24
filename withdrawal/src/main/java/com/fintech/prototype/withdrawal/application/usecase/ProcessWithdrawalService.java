package com.fintech.prototype.withdrawal.application.usecase;

import com.fintech.prototype.withdrawal.application.port.in.ProcessWithdrawalInboundPort;
import com.fintech.prototype.withdrawal.application.port.out.RepositoryOutboundPort;
import com.fintech.prototype.withdrawal.application.port.out.IdempotencyOutboundPort;
import com.fintech.prototype.withdrawal.application.usecase.withdrawal.ProcessWithdrawalCommand;
import com.fintech.prototype.withdrawal.application.usecase.withdrawal.ProcessWithdrawalResult;
import com.fintech.prototype.withdrawal.domain.model.Withdrawal;
import org.springframework.stereotype.Service;

@Service
public class ProcessWithdrawalService implements ProcessWithdrawalInboundPort {

    private final RepositoryOutboundPort repositoryOutboundPort;
    private final IdempotencyOutboundPort idempotencyOutboundPort;

    public ProcessWithdrawalService(RepositoryOutboundPort repositoryOutboundPort, IdempotencyOutboundPort idempotencyOutboundPort) {
        this.repositoryOutboundPort = repositoryOutboundPort;
        this.idempotencyOutboundPort = idempotencyOutboundPort;
    }

    @Override
    public ProcessWithdrawalResult process(ProcessWithdrawalCommand command) {
        if (command.idempotencyKey() != null && !command.idempotencyKey().isBlank()
                && !idempotencyOutboundPort.reserve(command.idempotencyKey())) {
            throw new IllegalStateException("Duplicated withdrawal request for idempotency key " + command.idempotencyKey());
        }

        repositoryOutboundPort.findById(command.identifier())
                .orElseThrow(() -> new IllegalStateException("Consultation session not found for identifier " + command.identifier()));

        if (command.password() == null || command.password().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        Withdrawal withdrawal = Withdrawal.approved(command.identifier(), command.amount());
        return new ProcessWithdrawalResult(withdrawal.withdrawalId(), withdrawal.status());
    }
}
