package com.fintech.prototype.consult.repository;

import com.fintech.prototype.consult.dto.CommonDataDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<CommonDataDTO, String> {
}
