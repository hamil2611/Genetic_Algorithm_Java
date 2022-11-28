package com.example.managefootballtournament.repository;

import com.example.managefootballtournament.entity.ScheduleInDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleInDayRepository extends JpaRepository<ScheduleInDay, Integer> {

    @Query(value = "select * from schedule_in_day s where s.schedule_id = :scheduleId", nativeQuery = true)
    List<ScheduleInDay> findByScheduleId(@Param("scheduleId") int scheduleId);
}
