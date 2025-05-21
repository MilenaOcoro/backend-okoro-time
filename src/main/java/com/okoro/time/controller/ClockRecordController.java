package com.okoro.time.controller;

import com.okoro.time.dto.request.ClockRecordRequest;
import com.okoro.time.dto.response.MessageResponse;
import com.okoro.time.model.ClockRecord;
import com.okoro.time.model.User;
import com.okoro.time.repository.UserRepository;
import com.okoro.time.service.ClockRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/clock-records")
@CrossOrigin(origins = "http://localhost:5173")
public class ClockRecordController {
   @Autowired
   ClockRecordService clockRecordService;
   
   @Autowired
   UserRepository userRepository;

   @GetMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<List<ClockRecord>> getAllClockRecords(
           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
       
       List<ClockRecord> clockRecords = clockRecordService.getAllClockRecords(date);
       return ResponseEntity.ok(clockRecords);
   }

   @GetMapping("/my-records")
   public ResponseEntity<List<ClockRecord>> getMyClockRecords(
           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
       
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String email = auth.getName();
       
       User user = userRepository.findByEmail(email)
           .orElseThrow(() -> new RuntimeException("User not found"));
       
       List<ClockRecord> clockRecords = clockRecordService.getClockRecordsByUser(user.getId(), date);
       return ResponseEntity.ok(clockRecords);
   }

   @GetMapping("/{id}")
   public ResponseEntity<ClockRecord> getClockRecordById(@PathVariable Long id) {
       ClockRecord clockRecord = clockRecordService.getClockRecordById(id);
       return ResponseEntity.ok(clockRecord);
   }

   @GetMapping("/latest")
   public ResponseEntity<ClockRecord> getLatestClockRecord() {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String email = auth.getName();
       
       User user = userRepository.findByEmail(email)
           .orElseThrow(() -> new RuntimeException("User not found"));
       
       ClockRecord clockRecord = clockRecordService.getLastClockRecord(user.getId());
       return ResponseEntity.ok(clockRecord);
   }

   @PostMapping
   public ResponseEntity<ClockRecord> createClockRecord(@Valid @RequestBody ClockRecordRequest clockRecordRequest) {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String email = auth.getName();
       
       User user = userRepository.findByEmail(email)
           .orElseThrow(() -> new RuntimeException("User not found"));
       
       ClockRecord clockRecord = clockRecordService.createClockRecord(user.getId(), clockRecordRequest);
       return ResponseEntity.ok(clockRecord);
   }

   @PutMapping("/{id}")
   public ResponseEntity<ClockRecord> updateClockRecord(
           @PathVariable Long id,
           @Valid @RequestBody ClockRecordRequest clockRecordRequest) {
       
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String email = auth.getName();
       
       User user = userRepository.findByEmail(email)
           .orElseThrow(() -> new RuntimeException("User not found"));
       
       ClockRecord clockRecord = clockRecordService.updateClockRecord(id, user.getId(), clockRecordRequest);
       return ResponseEntity.ok(clockRecord);
   }

   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<?> deleteClockRecord(@PathVariable Long id) {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String email = auth.getName();
       
       User user = userRepository.findByEmail(email)
           .orElseThrow(() -> new RuntimeException("User not found"));
       
       clockRecordService.deleteClockRecord(id, user.getId());
       return ResponseEntity.ok(new MessageResponse("Clock record successfully deleted"));
   }
}