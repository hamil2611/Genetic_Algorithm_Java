package com.example.managefootballtournament.repository;

import com.example.managefootballtournament.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlot,Integer> {
}
