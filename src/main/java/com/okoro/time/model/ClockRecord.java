package com.okoro.time.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "clock_records")
public class ClockRecord {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne()
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   @Column(name = "user_id", insertable = false, updatable = false)
   private Long userId;

   @NotBlank
   @Size(max = 20)
   private String type; // "clock_in" or "clock_out"

   @NotNull
   private LocalDate date;

   @NotNull
   private LocalTime time;

   @Size(max = 200)
   private String comments;

   private boolean manual;

   public ClockRecord() {
   }

   public ClockRecord(User user, String type, LocalDate date, LocalTime time, String comments, boolean manual) {
       this.user = user;
       this.type = type;
       this.date = date;
       this.time = time;
       this.comments = comments;
       this.manual = manual;
   }

   public Long getId() {
       return id;
   }

   public void setId(Long id) {
       this.id = id;
   }

   public User getUser() {
       return user;
   }

   public void setUser(User user) {
       this.user = user;
   }
   
   public Long getUserId() {
       return userId;
   }

   public String getType() {
       return type;
   }

   public void setType(String type) {
       this.type = type;
   }

   public LocalDate getDate() {
       return date;
   }

   public void setDate(LocalDate date) {
       this.date = date;
   }

   public LocalTime getTime() {
       return time;
   }

   public void setTime(LocalTime time) {
       this.time = time;
   }

   public String getComments() {
       return comments;
   }

   public void setComments(String comments) {
       this.comments = comments;
   }

   public boolean isManual() {
       return manual;
   }

   public void setManual(boolean manual) {
       this.manual = manual;
   }
}