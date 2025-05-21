package com.okoro.time.service;

import com.okoro.time.dto.request.ClockRecordRequest;
import com.okoro.time.exception.ResourceNotFoundException;
import com.okoro.time.exception.UnauthorizedException;
import com.okoro.time.model.ClockRecord;
import com.okoro.time.model.User;
import com.okoro.time.repository.ClockRecordRepository;
import com.okoro.time.repository.UserRepository;
import com.okoro.time.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ClockRecordService {
   @Autowired
   ClockRecordRepository clockRecordRepository;

   @Autowired
   UserRepository userRepository;

   public List<ClockRecord> getAllClockRecords(LocalDate date) {
       if (date != null) {
           return clockRecordRepository.findByDate(date);
       }
       return clockRecordRepository.findAll();
   }

   public List<ClockRecord> getClockRecordsByUser(Long userId, LocalDate date) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

       if (date != null) {
           return clockRecordRepository.findByUserIdAndDate(userId, date);
       }
       return clockRecordRepository.findByUserId(userId);
   }

   public ClockRecord getClockRecordById(Long id) {
       return clockRecordRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("ClockRecord", "id", id));
   }

   public ClockRecord createClockRecord(Long userId, ClockRecordRequest clockRecordRequest) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

       ClockRecord clockRecord = new ClockRecord();
       clockRecord.setUser(user);
       clockRecord.setType(clockRecordRequest.getType());
       clockRecord.setDate(clockRecordRequest.getDateAsLocalDate());
       clockRecord.setTime(clockRecordRequest.getTimeAsLocalTime());
       clockRecord.setComments(clockRecordRequest.getComments());
       clockRecord.setManual(clockRecordRequest.isManual());

       return clockRecordRepository.save(clockRecord);
   }

   public ClockRecord updateClockRecord(Long id, Long userId, ClockRecordRequest clockRecordRequest) {
       ClockRecord clockRecord = getClockRecordById(id);

       if (!userId.equals(clockRecord.getUserId()) && 
           !userRepository.findById(userId).orElseThrow().getRole().equals(Constants.ROLE_ADMIN)) {
           throw new UnauthorizedException(Constants.MSG_NO_AUTORIZADO);
       }

       clockRecord.setType(clockRecordRequest.getType());
       clockRecord.setDate(clockRecordRequest.getDateAsLocalDate());
       clockRecord.setTime(clockRecordRequest.getTimeAsLocalTime());
       clockRecord.setComments(clockRecordRequest.getComments());
       clockRecord.setManual(clockRecordRequest.isManual());

       return clockRecordRepository.save(clockRecord);
   }

   public void deleteClockRecord(Long id, Long userId) {
       ClockRecord clockRecord = getClockRecordById(id);

       if (!userRepository.findById(userId).orElseThrow().getRole().equals(Constants.ROLE_ADMIN)) {
           throw new UnauthorizedException(Constants.MSG_NO_AUTORIZADO);
       }

       clockRecordRepository.delete(clockRecord);
   }

   public ClockRecord getLastClockRecord(Long userId) {
       return clockRecordRepository.findLastClockRecordByUserId(userId);
   }
}