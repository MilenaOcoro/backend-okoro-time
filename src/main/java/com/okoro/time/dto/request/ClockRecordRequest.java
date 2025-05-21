package com.okoro.time.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public class ClockRecordRequest {
   @NotBlank
   @Size(max = 20)
   private String type;

   @NotNull
   private String date; // Format: YYYY-MM-DD

   @NotNull
   private String time; // Format: HH:MM:SS

   @Size(max = 200)
   private String comments;

   private boolean manual;

   public String getType() {
       return type;
   }

   public void setType(String type) {
       this.type = type;
   }

   public String getDate() {
       return date;
   }

   public void setDate(String date) {
       this.date = date;
   }

   public String getTime() {
       return time;
   }

   public void setTime(String time) {
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
   
   public LocalDate getDateAsLocalDate() {
       return LocalDate.parse(this.date);
   }
   
   public LocalTime getTimeAsLocalTime() {
       return LocalTime.parse(this.time);
   }
}