package com.fintech.prototype.consulta.application.port.in;

import com.fintech.prototype.consulta.application.usecase.consultation.ProcessConsultationCommand;
import com.fintech.prototype.consulta.application.usecase.consultation.ProcessConsultationResult;

public interface ProcessConsultationInboundPort {
    ProcessConsultationResult process(ProcessConsultationCommand command);
}
