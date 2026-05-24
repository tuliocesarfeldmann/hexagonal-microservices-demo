package com.fintech.prototype.withdrawal.repository;

import com.fintech.prototype.withdrawal.dto.CommonDataDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<CommonDataDTO, String> {
}
