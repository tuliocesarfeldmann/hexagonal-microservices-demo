package com.fintech.prototype.consult.application.port.in;

import com.fintech.prototype.consult.application.usecase.consultation.ProcessConsultationCommand;
import com.fintech.prototype.consult.application.usecase.consultation.ProcessConsultationResult;

public interface ProcessConsultationInboundPort {
    ProcessConsultationResult process(ProcessConsultationCommand command);
}
