package com.rentit.tax.api;

public class TaxResponse {

  private boolean isValid;

  public TaxResponse(boolean isValid) {
    this.isValid = isValid;
  }

  public boolean isValid() {
    return isValid;
  }

  public void setValid(boolean valid) {
    isValid = valid;
  }
}
