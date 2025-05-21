package com.okoro.time.service;

import com.okoro.time.dto.request.UserRequest;
import com.okoro.time.exception.ResourceNotFoundException;
import com.okoro.time.exception.UnauthorizedException;
import com.okoro.time.model.User;
import com.okoro.time.repository.UserRepository;
import com.okoro.time.util.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
   @Autowired
   UserRepository userRepository;

   @Autowired
   PasswordEncoder passwordEncoder;

   public List<User> getAllUsers() {
       return userRepository.findAll();
   }

   public User getUserById(Long id) {
       return userRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
   }

   public Optional<User> getUserByEmail(String email) {
       return userRepository.findByEmail(email);
   }

   public User createUser(UserRequest userRequest) {
       if (userRepository.existsByEmail(userRequest.getEmail())) {
           throw new RuntimeException(Constants.MSG_EMAIL_YA_EXISTE);
       }

       User user = new User();
       user.setName(userRequest.getName());
       user.setEmail(userRequest.getEmail());
       user.setRole(userRequest.getRole());

       if (Boolean.TRUE.equals(userRequest.getGeneratePassword())) {
           String randomPassword = generateRandomPassword();
           user.setPassword(passwordEncoder.encode(randomPassword));
       } else {
           user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
       }

       return userRepository.save(user);
   }

   public User updateUser(Long id, UserRequest userRequest) {
       User user = getUserById(id);

       user.setName(userRequest.getName());
       
       if (!user.getEmail().equals(userRequest.getEmail())) {
           if (userRepository.existsByEmail(userRequest.getEmail())) {
               throw new RuntimeException(Constants.MSG_EMAIL_YA_EXISTE);
           }
           user.setEmail(userRequest.getEmail());
       }
       
       user.setRole(userRequest.getRole());
       
       if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
           user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
       }

       return userRepository.save(user);
   }

   public void deleteUser(Long id) {
       User user = getUserById(id);
       userRepository.delete(user);
   }

   public void changePassword(Long userId, String oldPassword, String newPassword) {
       User user = getUserById(userId);
       
       if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
           throw new UnauthorizedException("Current password is incorrect");
       }
       
       user.setPassword(passwordEncoder.encode(newPassword));
       userRepository.save(user);
   }

   private String generateRandomPassword() {
       return RandomStringUtils.randomAlphanumeric(10);
   }
}