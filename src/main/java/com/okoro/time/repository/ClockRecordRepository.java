package com.okoro.time.repository;

import com.okoro.time.model.ClockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClockRecordRepository extends JpaRepository<ClockRecord, Long> {
   List<ClockRecord> findByUserId(Long userId);
   
   List<ClockRecord> findByUserIdAndDate(Long userId, LocalDate date);
   
   List<ClockRecord> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
   
   List<ClockRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);
   
   List<ClockRecord> findByDate(LocalDate date);
   
   @Query("SELECT c FROM ClockRecord c WHERE c.userId = :userId ORDER BY c.date DESC, c.time DESC LIMIT 1")
   ClockRecord findLastClockRecordByUserId(@Param("userId") Long userId);
   
   @Query("SELECT COUNT(DISTINCT c.date) FROM ClockRecord c WHERE c.userId = :userId AND c.date BETWEEN :startDate AND :endDate")
   Integer countDistinctDateByUserIdAndDateBetween(
           @Param("userId") Long userId, 
           @Param("startDate") LocalDate startDate, 
           @Param("endDate") LocalDate endDate);
   
   @Query(value = "SELECT SUM(EXTRACT(EPOCH FROM " +
           "(SELECT MIN(clockout.time) FROM clock_records clockout WHERE clockout.user_id = clockin.user_id " +
           "AND clockout.date = clockin.date AND clockout.type = 'clock_out' AND clockout.time > clockin.time) - clockin.time) / 60) AS total_minutes " +
           "FROM clock_records clockin " +
           "WHERE clockin.user_id = :userId AND clockin.type = 'clock_in' " +
           "AND clockin.date BETWEEN :startDate AND :endDate " +
           "AND EXISTS (SELECT 1 FROM clock_records clockout WHERE clockout.user_id = clockin.user_id " +
           "AND clockout.date = clockin.date AND clockout.type = 'clock_out' AND clockout.time > clockin.time)", 
           nativeQuery = true)
   Integer calculateMinutesWorkedByUser(
           @Param("userId") Long userId, 
           @Param("startDate") LocalDate startDate, 
           @Param("endDate") LocalDate endDate);
}