package com.rentit.tax.controller;

import com.rentit.tax.api.TaxRequest;
import com.rentit.tax.api.TaxResponse;
import com.rentit.tax.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("taxes")
public class TaxController {

  private final TaxService taxService;

  @PostMapping("/check")
  public TaxResponse check(@RequestBody TaxRequest taxRequest) {
    return taxService.check(taxRequest);
  }
}
