package com.example.managefootballtournament.repository;

import com.example.managefootballtournament.entity.Schedule;
import com.example.managefootballtournament.entity.ScheduleInDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
}
