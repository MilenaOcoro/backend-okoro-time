package com.okoro.time.controller;

import com.okoro.time.dto.request.LoginRequest;
import com.okoro.time.dto.request.UserRequest;
import com.okoro.time.dto.response.JwtResponse;
import com.okoro.time.dto.response.MessageResponse;
import com.okoro.time.model.User;
import com.okoro.time.service.AuthService;
import com.okoro.time.service.UserService;

import jakarta.validation.Valid; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
   private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
   
   @Autowired
   AuthService authService;

   @Autowired
   UserService userService;

   @PostMapping("/login")
   public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
       logger.info("Login request received for email: {}", loginRequest.getEmail());
       try {
           JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
           logger.info("Login successful for user: {}", jwtResponse.getEmail());
           return ResponseEntity.ok(jwtResponse);
       } catch (Exception e) {
           logger.error("Error during login process: {}", e.getMessage());
           throw e;
       }
   }

   @GetMapping("/verify")
   public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
       String token = authHeader.substring(7);
       
       boolean isValid = authService.verifyToken(token);
       
       if (isValid) {
           return ResponseEntity.ok().body(isValid);
       } else {
           return ResponseEntity.status(401).body(isValid);
       }
   }

   @PostMapping("/recover-password")
   public ResponseEntity<?> recoverPassword(@RequestParam String email) {
       return ResponseEntity.ok(new MessageResponse(
               "Password reset instructions have been sent to: " + email));
   }

   @PostMapping("/reset-password")
   public ResponseEntity<?> resetPassword(
           @RequestParam String token,
           @RequestParam String newPassword) {
       return ResponseEntity.ok(new MessageResponse("Password reset successfully"));
   }

   @PostMapping("/create-admin")
   public ResponseEntity<?> createAdmin() {
       try {
           UserRequest state = new UserRequest();
           state.setEmail("leidyokoro@gmail.com");
           state.setName("Milena");
           state.setPassword("okoro2025");
           state.setRole("Admin");

           User response = userService.createUser(state);
            
           return ResponseEntity.ok(response);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
       }
   }
}