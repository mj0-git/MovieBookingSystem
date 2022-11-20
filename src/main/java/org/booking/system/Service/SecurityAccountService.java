package org.booking.system.Service;

import org.booking.system.DTO.Account;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

// Interface
public interface SecurityAccountService {

    //Security
    String login(String username, String password);
    Optional<User> findByToken(String token);
    // Account findById(Long id);
}