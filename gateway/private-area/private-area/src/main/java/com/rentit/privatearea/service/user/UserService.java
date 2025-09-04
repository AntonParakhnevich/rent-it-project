package com.rentit.privatearea.service.user;

import com.rentit.user.api.UserConnector;
import com.rentit.user.api.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserConnector userConnector;

  public UserResponse getById(Long id) {
    return userConnector.getUserById(id);
  }

}
