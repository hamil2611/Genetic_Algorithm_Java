package com.example.managefootballtournament.repository;

import com.example.managefootballtournament.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match,Integer> {
}
