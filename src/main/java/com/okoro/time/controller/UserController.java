package com.okoro.time.controller;

import com.okoro.time.dto.request.UserRequest;
import com.okoro.time.dto.response.MessageResponse;
import com.okoro.time.model.User;
import com.okoro.time.repository.UserRepository;
import com.okoro.time.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
   @Autowired
   UserService userService;
   
   @Autowired
   UserRepository userRepository;

   @GetMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<List<User>> getAllUsers() {
       List<User> users = userService.getAllUsers();
       
       for (User user : users) {
           user.setPassword(null);
       }
       
       return ResponseEntity.ok(users);
   }

   @GetMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
   public ResponseEntity<User> getUserById(@PathVariable Long id) {
       User user = userService.getUserById(id);
       user.setPassword(null);
       return ResponseEntity.ok(user);
   }

   @PostMapping 
   public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest) {
       User user = userService.createUser(userRequest);
       user.setPassword(null);
       
       if (Boolean.TRUE.equals(userRequest.getGeneratePassword())) {
           Map<String, Object> response = new HashMap<>();
           response.put("user", user);
           response.put("passwordGenerated", true);
           response.put("message", "User created with temporary password");
           return ResponseEntity.ok(response);
       }
       
       return ResponseEntity.ok(user);
   }

   @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
   public ResponseEntity<?> updateUser(
           @PathVariable Long id,
           @Valid @RequestBody UserRequest userRequest) {
       
       User user = userService.updateUser(id, userRequest);
       user.setPassword(null);
       return ResponseEntity.ok(user);
   }

   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<?> deleteUser(@PathVariable Long id) {
       userService.deleteUser(id);
       return ResponseEntity.ok(new MessageResponse("User successfully deleted"));
   }

   @PostMapping("/change-password")
   public ResponseEntity<?> changePassword(
           @RequestParam Long id,
           @RequestParam String oldPassword,
           @RequestParam String newPassword) {
       
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String email = auth.getName();
       
       User currentUser = userRepository.findByEmail(email)
           .orElseThrow(() -> new RuntimeException("User not found"));
       
       if (!currentUser.getId().equals(id) && !currentUser.getRole().equalsIgnoreCase("ADMIN")) {
           return ResponseEntity.status(403).body(new MessageResponse("Not authorized to change another user's password"));
       }
       
       userService.changePassword(id, oldPassword, newPassword);
       return ResponseEntity.ok(new MessageResponse("Password successfully updated"));
   }
}