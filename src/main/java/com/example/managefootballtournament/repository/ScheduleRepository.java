package com.example.managefootballtournament.repository;

import com.example.managefootballtournament.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
    @Query(value = "select s from Schedule s where s.tournament.id = :tournamentId")
    List<Schedule> findAllByTournamentId(@Param("tournamentId") int tournamentId);
}
