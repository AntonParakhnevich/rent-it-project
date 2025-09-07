package com.rentit.tax.service;

import com.rentit.tax.api.TaxRequest;
import com.rentit.tax.api.TaxResponse;
import com.rentit.tax.client.TaxExternalClient;
import com.rentit.tax.client.TaxExternalResponse;
import com.rentit.tax.model.Tax;
import com.rentit.tax.model.TaxStatus;
import com.rentit.tax.repository.TaxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaxService {

  private final TaxRepository taxRepository;
  private final TaxExternalClient taxClient;

  public TaxResponse check(TaxRequest request) {
    TaxExternalResponse data = taxClient.getData(request.getUnp());
    return new TaxResponse(data.getRaw().getVkods().equals(TaxStatus.ACTIVE.getName()));
  }

  public void create() {
    Tax tax = new Tax();
    taxRepository.save(tax);
  }
}
