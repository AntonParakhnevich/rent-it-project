package com.rentit.privatearea.security;

import com.rentit.privatearea.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

  private final HttpServletRequest httpServletRequest;

  public Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      return userDetails.getUserId();
    }
    return null;
  }
}
