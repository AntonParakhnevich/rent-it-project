package com.rentit.tax.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "taxExternalClient", url = "grp.nalog.gov.by") // Or use service name for Eureka
public interface TaxExternalClient {

  @GetMapping("/api/grp-public/data?unp={unp}")
  TaxExternalResponse getData(@PathVariable("unp") String unp);

}
