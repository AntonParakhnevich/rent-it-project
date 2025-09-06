package com.rentit.user.api;

import java.util.Set;

public class AuthResponse {

  private String accessToken;
  private Long userId;
  private String email;
  private String firstName;
  private String lastName;
  private Set<String> roles;

  public AuthResponse() {
  }

  public AuthResponse(String accessToken, Long userId, String email, String firstName, String lastName, Set<String> roles) {
    this.accessToken = accessToken;
    this.userId = userId;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roles = roles;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }
}
