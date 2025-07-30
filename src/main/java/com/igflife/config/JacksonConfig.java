package com.igflife.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

@Singleton
public class JacksonConfig implements ObjectMapperCustomizer {
    public void customize(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());  // For LocalDateTime support
    }
}