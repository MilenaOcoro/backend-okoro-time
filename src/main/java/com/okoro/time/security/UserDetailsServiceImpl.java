package com.okoro.time.security;

import com.okoro.time.model.User;
import com.okoro.time.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
   private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
   
   @Autowired
   UserRepository userRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       logger.info("Looking for user by email: {}", email);
       
       User user = userRepository.findByEmail(email)
               .orElseThrow(() -> {
                   logger.error("User not found with email: {}", email);
                   return new UsernameNotFoundException("User not found with email: " + email);
               });

       logger.info("User found: ID={}, Role={}", user.getId(), user.getRole());
       
       List<SimpleGrantedAuthority> authorities = new ArrayList<>();
       authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
       
       return new org.springframework.security.core.userdetails.User(
               user.getEmail(),
               user.getPassword(),
               authorities);
   }
}