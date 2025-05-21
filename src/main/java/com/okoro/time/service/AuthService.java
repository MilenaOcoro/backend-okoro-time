package com.okoro.time.service;

import com.okoro.time.dto.request.LoginRequest;
import com.okoro.time.dto.response.JwtResponse;
import com.okoro.time.exception.UnauthorizedException;
import com.okoro.time.model.User;
import com.okoro.time.repository.UserRepository;
import com.okoro.time.security.JwtUtils;
import com.okoro.time.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
   private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
   
   @Autowired
   AuthenticationManager authenticationManager;

   @Autowired
   UserRepository userRepository;

   @Autowired
   JwtUtils jwtUtils;

   public JwtResponse authenticateUser(LoginRequest loginRequest) {
       logger.info("Starting authentication process for email: {}", loginRequest.getEmail());
       
       try {
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

           logger.info("Authentication successful. Generating JWT token.");
           
           SecurityContextHolder.getContext().setAuthentication(authentication);
           
           String jwt = jwtUtils.generateJwtToken(authentication);
           
           UserDetails userDetails = (UserDetails) authentication.getPrincipal();
           String email = userDetails.getUsername();
           
           User user = userRepository.findByEmail(email)
                   .orElseThrow(() -> {
                       logger.error("User not found after authentication: {}", email);
                       return new UnauthorizedException(Constants.MSG_CREDENCIALES_INVALIDAS);
                   });
           
           logger.info("Authentication and token generation completed for user ID: {}", user.getId());
           
           return new JwtResponse(
                   jwt,
                   user.getId(),
                   user.getName(),
                   user.getEmail(),
                   user.getRole());
       } catch (Exception e) {
           logger.error("Error during authentication: {}", e.getMessage());
           throw e;
       }
   }

   public boolean verifyToken(String token) {
       try {
           if (jwtUtils.validateJwtToken(token)) {
               String email = jwtUtils.getUsernameFromJwtToken(token);
               return userRepository.findByEmail(email).isPresent();
           }
           return false;
       } catch (Exception e) {
           logger.error("Error verifying token: {}", e.getMessage());
           return false;
       }
   }
}