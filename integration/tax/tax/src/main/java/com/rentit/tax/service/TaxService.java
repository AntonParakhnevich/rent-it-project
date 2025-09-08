package com.rentit.tax.service;

import com.rentit.tax.api.TaxRequest;
import com.rentit.tax.api.TaxResponse;
import com.rentit.tax.client.TaxExternalClient;
import com.rentit.tax.client.TaxExternalResponse;
import com.rentit.tax.model.Tax;
import com.rentit.tax.model.TaxStatus;
import com.rentit.tax.repository.TaxRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaxService {

  private final TaxRepository taxRepository;
  private final TaxExternalClient taxClient;

  public TaxResponse check(TaxRequest request) {
    try {
      TaxExternalResponse data = taxClient.getData(request.getUnp());
      return new TaxResponse(data.getRow().getVkods().equals(TaxStatus.ACTIVE.getName()));

    } catch (Exception e) {
      System.out.println(e);
    }
    return new TaxResponse();
  }

  public void create(TaxExternalResponse response, Long userId) {
    Tax tax = new Tax();
    tax.setDateRequested(LocalDateTime.now());
    tax.setUserId(userId);
    taxRepository.save(tax);
  }
}
