package com.rentit.tax.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "taxConnector", url = "localhost:8082")
public interface TaxConnector {

  @GetMapping("/taxes/check")
  TaxResponse check(TaxRequest request);
}
