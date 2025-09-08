package com.rentit.tax.api;

public class TaxRequest {

  private String unp;
  private String firstName;
  private String lastName;
  private Long userId;

  public TaxRequest(String unp, String firstName, String lastName, Long userId) {
    this.unp = unp;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userId = userId;
  }

  public String getUnp() {
    return unp;
  }

  public void setUnp(String unp) {
    this.unp = unp;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
