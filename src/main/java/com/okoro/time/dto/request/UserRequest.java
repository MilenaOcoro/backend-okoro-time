package com.okoro.time.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {
   @NotBlank
   @Size(max = 100)
   private String name;

   @NotBlank
   @Size(max = 100)
   @Email
   private String email;

   @Size(max = 120)
   private String password;

   @NotBlank
   @Size(max = 20)
   private String role;
   
   private Boolean generatePassword;

   public String getName() {
       return name;
   }

   public void setName(String name) {
       this.name = name;
   }

   public String getEmail() {
       return email;
   }

   public void setEmail(String email) {
       this.email = email;
   }

   public String getPassword() {
       return password;
   }

   public void setPassword(String password) {
       this.password = password;
   }

   public String getRole() {
       return role;
   }

   public void setRole(String role) {
       this.role = role;
   }
   
   public Boolean getGeneratePassword() {
       return generatePassword;
   }
   
   public void setGeneratePassword(Boolean generatePassword) {
       this.generatePassword = generatePassword;
   }
}