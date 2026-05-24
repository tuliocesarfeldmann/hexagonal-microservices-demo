package com.fintech.prototype.withdrawal.application.port.in;

import com.fintech.prototype.withdrawal.application.usecase.withdrawal.ProcessWithdrawalCommand;
import com.fintech.prototype.withdrawal.application.usecase.withdrawal.ProcessWithdrawalResult;

public interface ProcessWithdrawalInboundPort {
    ProcessWithdrawalResult process(ProcessWithdrawalCommand command);
}
