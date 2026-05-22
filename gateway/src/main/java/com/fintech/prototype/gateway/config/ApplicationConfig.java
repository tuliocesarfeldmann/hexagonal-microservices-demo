package com.fintech.prototype.gateway.config;

import com.fintech.prototype.gateway.application.MessagingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MessagingProperties.class)
public class ApplicationConfig {
}
