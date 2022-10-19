package com.example.managefootballtournament.repository;

import com.example.managefootballtournament.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament,Integer> {
}
