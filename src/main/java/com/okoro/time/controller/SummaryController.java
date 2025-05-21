package com.okoro.time.controller;

import com.okoro.time.dto.response.SummaryResponse;
import com.okoro.time.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/clock-records/summary")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:5173")
public class SummaryController {
   @Autowired
   SummaryService summaryService;

   @GetMapping
   public ResponseEntity<SummaryResponse> getSummary(
           @RequestParam String period,
           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
           @RequestParam(required = false) Long userId) {
       
       SummaryResponse summary = summaryService.getSummary(period, startDate, endDate, userId);
       return ResponseEntity.ok(summary);
   }
}