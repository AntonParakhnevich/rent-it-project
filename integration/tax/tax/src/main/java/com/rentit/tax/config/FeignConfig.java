package com.rentit.tax.config;

import com.rentit.tax.client.TaxErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FeignConfig {

  private final ObjectMapperConfig objectMapperConfig;

  @Bean
  public TaxErrorDecoder decoder() {
    return new TaxErrorDecoder(objectMapperConfig.objectMapper());
  }
}
