package com.okoro.time.dto.response;

import java.util.List;
import java.util.Map;

public class SummaryResponse {
   private List<UserSummary> users;
   private Totals totals;
   
   public static class UserSummary {
       private Long id;
       private String name;
       private int daysWorked;
       private int totalMinutes;
       private int averageMinutesDaily;
       
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
       
       public int getDaysWorked() {
           return daysWorked;
       }
       
       public void setDaysWorked(int daysWorked) {
           this.daysWorked = daysWorked;
       }
       
       public int getTotalMinutes() {
           return totalMinutes;
       }
       
       public void setTotalMinutes(int totalMinutes) {
           this.totalMinutes = totalMinutes;
       }
       
       public int getAverageMinutesDaily() {
           return averageMinutesDaily;
       }
       
       public void setAverageMinutesDaily(int averageMinutesDaily) {
           this.averageMinutesDaily = averageMinutesDaily;
       }
   }
   
   public static class Totals {
       private int daysWorked;
       private int totalMinutes;
       private int averageMinutesDaily;
       
       public int getDaysWorked() {
           return daysWorked;
       }
       
       public void setDaysWorked(int daysWorked) {
           this.daysWorked = daysWorked;
       }
       
       public int getTotalMinutes() {
           return totalMinutes;
       }
       
       public void setTotalMinutes(int totalMinutes) {
           this.totalMinutes = totalMinutes;
       }
       
       public int getAverageMinutesDaily() {
           return averageMinutesDaily;
       }
       
       public void setAverageMinutesDaily(int averageMinutesDaily) {
           this.averageMinutesDaily = averageMinutesDaily;
       }
   }
   
   public List<UserSummary> getUsers() {
       return users;
   }
   
   public void setUsers(List<UserSummary> users) {
       this.users = users;
   }
   
   public Totals getTotals() {
       return totals;
   }
   
   public void setTotals(Totals totals) {
       this.totals = totals;
   }
}