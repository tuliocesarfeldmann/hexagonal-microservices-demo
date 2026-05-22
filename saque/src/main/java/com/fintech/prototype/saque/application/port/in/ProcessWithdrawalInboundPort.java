package com.fintech.prototype.saque.application.port.in;

import com.fintech.prototype.saque.application.usecase.withdrawal.ProcessWithdrawalCommand;
import com.fintech.prototype.saque.application.usecase.withdrawal.ProcessWithdrawalResult;

public interface ProcessWithdrawalInboundPort {
    ProcessWithdrawalResult process(ProcessWithdrawalCommand command);
}
