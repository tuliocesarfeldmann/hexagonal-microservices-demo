package com.fintech.prototype.consult.adapter.out.redis;

import com.fintech.prototype.consult.application.port.out.RepositoryOutboundPort;
import com.fintech.prototype.consult.domain.model.ConsultationSession;
import com.fintech.prototype.consult.dto.CommonDataDTO;
import com.fintech.prototype.consult.repository.RedisRepository;
import org.springframework.stereotype.Component;

@Component
public class RedisRepositoryOutboundAdapter implements RepositoryOutboundPort {

    private final RedisRepository repository;

    public RedisRepositoryOutboundAdapter(RedisRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ConsultationSession session) {
        repository.save(CommonDataDTO.builder()
                .identifier(session.identifier())
                .agency(session.agency())
                .account(session.account())
                .document(session.document())
                .name(session.name())
                .build());
    }
}
