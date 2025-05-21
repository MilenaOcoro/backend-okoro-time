package com.okoro.time.service;

import com.okoro.time.dto.response.SummaryResponse;
import com.okoro.time.model.User;
import com.okoro.time.repository.ClockRecordRepository;
import com.okoro.time.repository.UserRepository;
import com.okoro.time.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class SummaryService {
   @Autowired
   ClockRecordRepository clockRecordRepository;

   @Autowired
   UserRepository userRepository;

   public SummaryResponse getSummary(String period, LocalDate startDate, LocalDate endDate, Long userId) {
       if (startDate == null || endDate == null) {
           LocalDate today = LocalDate.now();
           
           switch (period) {
               case Constants.PERIODO_DIA:
                   startDate = today;
                   endDate = today;
                   break;
               case Constants.PERIODO_SEMANA:
                   startDate = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                   endDate = startDate.plusDays(6);
                   break;
               case Constants.PERIODO_MES:
                   startDate = today.withDayOfMonth(1);
                   endDate = today.with(TemporalAdjusters.lastDayOfMonth());
                   break;
               default:
                   startDate = today;
                   endDate = today;
           }
       }

       SummaryResponse summary = new SummaryResponse();
       List<SummaryResponse.UserSummary> userList = new ArrayList<>();
       
       List<User> users;
       if (userId != null) {
           users = new ArrayList<>();
           users.add(userRepository.findById(userId).orElse(null));
       } else {
           users = userRepository.findAll();
       }
       
       int totalDaysWorked = 0;
       int totalMinutes = 0;
       
       for (User user : users) {
           if (user == null) continue;
           
           SummaryResponse.UserSummary userSummary = new SummaryResponse.UserSummary();
           userSummary.setId(user.getId());
           userSummary.setName(user.getName());
           
           int daysWorked = clockRecordRepository.countDistinctDateByUserIdAndDateBetween(
                   user.getId(), startDate, endDate);
           userSummary.setDaysWorked(daysWorked);
           
           Integer minutesWorked = clockRecordRepository.calculateMinutesWorkedByUser(
                   user.getId(), startDate, endDate);
           
           if (minutesWorked == null) {
               minutesWorked = 0;
           }
           
           userSummary.setTotalMinutes(minutesWorked);
           
           int averageMinutesDaily = daysWorked > 0 ? 
                   minutesWorked / daysWorked : 0;
           userSummary.setAverageMinutesDaily(averageMinutesDaily);
           
           userList.add(userSummary);
           
           totalDaysWorked += daysWorked;
           totalMinutes += minutesWorked;
       }
       
       summary.setUsers(userList);
       
       SummaryResponse.Totals totals = new SummaryResponse.Totals();
       totals.setDaysWorked(totalDaysWorked);
       totals.setTotalMinutes(totalMinutes);
       
       int globalAverage = userList.size() > 0 ? 
               totalMinutes / userList.size() : 0;
       totals.setAverageMinutesDaily(globalAverage);
       
       summary.setTotals(totals);
       
       return summary;
   }
}