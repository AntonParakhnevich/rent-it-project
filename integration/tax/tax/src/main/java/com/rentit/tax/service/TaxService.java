package com.rentit.tax.service;

import com.rentit.tax.api.TaxRequest;
import com.rentit.tax.api.TaxResponse;
import com.rentit.tax.client.TaxExternalClient;
import com.rentit.tax.client.TaxExternalResponse;
import com.rentit.tax.exception.UserTaxNotFoundException;
import com.rentit.tax.model.Tax;
import com.rentit.tax.model.TaxStatus;
import com.rentit.tax.repository.TaxRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaxService {

  private final TaxRepository taxRepository;
  private final TaxExternalClient taxClient;
  private static final Logger log = LoggerFactory.getLogger(TaxService.class);

  public TaxResponse check(TaxRequest request) {
    TaxExternalResponse data = null;
    TaxStatus status;
    try {
      data = taxClient.getData(request.getUnp());
      status = TaxStatus.getStatusByValue(data.getRow().getVkods());
    } catch (Exception e) {
      log.error(String.format("Error handle request to tax, userId=%s", request.getUserId()), e);
      if (e instanceof UserTaxNotFoundException) {
        status = TaxStatus.NOT_FOUND;
      } else {
        status = TaxStatus.ERROR;
      }
    }
    create(request.getUserId(), status);
    boolean isValid = TaxStatus.ACTIVE.equals(status)
        && getFullName(request).equals(data.getRow().getVnaimp());
    return new TaxResponse(isValid);
  }

  public void create(Long userId, TaxStatus status) {
    Tax tax = new Tax();
    tax.setDateRequested(LocalDateTime.now());
    tax.setUserId(userId);
    tax.setStatus(status);
    taxRepository.save(tax);
  }

  private String getFullName(TaxRequest request) {
    return request.getLastName() + " " + request.getFirstName() + " " + request.getPatronymic();
  }
}
