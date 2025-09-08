package com.rentit.tax.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentit.tax.exception.UserTaxNotFoundException;
import com.rentit.tax.model.TaxStatus;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaxErrorDecoder implements ErrorDecoder {

  private final ObjectMapper objectMapper;

  @Override
  public Exception decode(String methodKey, Response response) {
    if (response.status() == 400) {
      try {
        ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
        if (errorResponse.getMessage().equals("Нет данных по запросу")) {
          return new UserTaxNotFoundException();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return new IllegalArgumentException(TaxStatus.ERROR.getName());
    }
    return new Default().decode(methodKey, response);
  }
}
