package com.okoro.time.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", 
      uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @NotBlank
   @Size(max = 100)
   private String name;

   @NotBlank
   @Size(max = 100)
   @Email
   private String email;

   @NotBlank
   @Size(max = 120)
   private String password;

   @NotBlank
   @Size(max = 20)
   private String role;
   
   @Column(name = "created_at")
   private LocalDateTime createdAt;
   
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   public User() {
   }

   public User(String name, String email, String password, String role) {
       this.name = name;
       this.email = email;
       this.password = password;
       this.role = role;
       this.createdAt = LocalDateTime.now();
       this.updatedAt = LocalDateTime.now();
   }

   public Long getId() {
       return id;
   }

   public void setId(Long id) {
       this.id = id;
   }

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
   
   public LocalDateTime getCreatedAt() {
       return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
       this.createdAt = createdAt;
   }

   public LocalDateTime getUpdatedAt() {
       return updatedAt;
   }

   public void setUpdatedAt(LocalDateTime updatedAt) {
       this.updatedAt = updatedAt;
   }
   
   @PrePersist
   protected void onCreate() {
       this.createdAt = LocalDateTime.now();
       this.updatedAt = LocalDateTime.now();
   }
   
   @PreUpdate
   protected void onUpdate() {
       this.updatedAt = LocalDateTime.now();
   }
}