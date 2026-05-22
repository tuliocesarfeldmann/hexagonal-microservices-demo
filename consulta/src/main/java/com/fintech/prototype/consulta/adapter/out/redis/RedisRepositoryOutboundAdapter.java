package com.fintech.prototype.consulta.adapter.out.redis;

import com.fintech.prototype.consulta.application.port.out.RepositoryOutboundPort;
import com.fintech.prototype.consulta.domain.model.ConsultationSession;
import com.fintech.prototype.consulta.dto.CommomDataDTO;
import com.fintech.prototype.consulta.repository.RedisRepository;
import org.springframework.stereotype.Component;

@Component
public class RedisRepositoryOutboundAdapter implements RepositoryOutboundPort {

    private final RedisRepository repository;

    public RedisRepositoryOutboundAdapter(RedisRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ConsultationSession session) {
        repository.save(CommomDataDTO.builder()
                .identifier(session.identifier())
                .agency(session.agency())
                .account(session.account())
                .document(session.document())
                .name(session.name())
                .build());
    }
}
