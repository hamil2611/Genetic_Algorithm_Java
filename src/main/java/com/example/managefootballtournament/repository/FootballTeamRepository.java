package com.example.managefootballtournament.repository;

import com.example.managefootballtournament.entity.FootballTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FootballTeamRepository extends JpaRepository<FootballTeam,Integer> {

    @Query(value = "select fb from FootballTeam fb " +
            "where fb.tournament.id = :tournamentId")
    List<FootballTeam> findAllFootballTeamOfTournament(int tournamentId);
}
