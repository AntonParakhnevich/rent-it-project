package com.rentit.tax.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class TaxExternalResponse {

  private TaxRaw row;

}
