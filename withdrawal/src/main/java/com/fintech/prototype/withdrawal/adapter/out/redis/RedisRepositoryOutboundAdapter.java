package com.fintech.prototype.withdrawal.adapter.out.redis;

import com.fintech.prototype.withdrawal.application.port.out.RepositoryOutboundPort;
import com.fintech.prototype.withdrawal.domain.model.ConsultationSession;
import com.fintech.prototype.withdrawal.repository.RedisRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RedisRepositoryOutboundAdapter implements RepositoryOutboundPort {

    private final RedisRepository repository;

    public RedisRepositoryOutboundAdapter(RedisRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ConsultationSession> findById(String identifier) {
        return repository.findById(identifier)
                .map(data -> new ConsultationSession(
                        data.getIdentifier(),
                        data.getAgency(),
                        data.getAccount(),
                        data.getDocument(),
                        data.getName()
                ));
    }
}
