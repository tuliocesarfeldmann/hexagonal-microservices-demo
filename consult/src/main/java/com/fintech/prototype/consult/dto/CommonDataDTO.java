package com.fintech.prototype.consult.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("CommonData")
public class CommonDataDTO {
    @Id
    private String identifier;

    private String agency;

    private String account;

    private String document;

    private String name;

}
