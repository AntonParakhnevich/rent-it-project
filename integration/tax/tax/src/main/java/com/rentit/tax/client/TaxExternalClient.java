package com.rentit.tax.client;

import com.rentit.tax.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "taxExternalClient", url = "grp.nalog.gov.by", configuration = FeignConfig.class) // Or use service name for
public interface TaxExternalClient {

  @GetMapping("/api/grp-public/data?unp={unp}")
  TaxExternalResponse getData(@PathVariable("unp") String unp);

}
