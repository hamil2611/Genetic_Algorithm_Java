package com.example.managefootballtournament.repository;

import com.example.managefootballtournament.entity.ScheduleInDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleInDay,Integer> {
    @Query(value = "select s from ScheduleInDay s where s.tournament.id = :tournamentId")
    List<ScheduleInDay> findAllByTournamentId(@Param("tournamentId") int tournamentId);
}
