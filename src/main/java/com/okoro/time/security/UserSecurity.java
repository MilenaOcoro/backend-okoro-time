package com.okoro.time.security;

import com.okoro.time.model.User;
import com.okoro.time.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {
   
   @Autowired
   private UserRepository userRepository;
   
   public boolean isCurrentUser(Long id) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       if (authentication == null) {
           return false;
       }
       
       String email = authentication.getName();
       
       return userRepository.findByEmail(email)
               .map(user -> user.getId().equals(id))
               .orElse(false);
   }
}