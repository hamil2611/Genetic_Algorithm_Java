package com.example.managefootballtournament.controller;

import com.example.managefootballtournament.domain.FootballTeamDTO;
import com.example.managefootballtournament.domain.TournamentDTO;
import com.example.managefootballtournament.entity.FootballTeam;
import com.example.managefootballtournament.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tournament")
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;

    // CRUD TOURNAMENT
    @PostMapping("/add-new-tournament")
    public ResponseEntity<Integer> addTournament(@Valid @RequestBody TournamentDTO tournamentDTO) {
        int result = tournamentService.addNewTournament(tournamentDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all-tournament")
    public ResponseEntity<List<TournamentDTO>> displayAllTournament() {
        return new ResponseEntity<>(tournamentService.getAllTournament(), HttpStatus.OK);
    }

    // CRUD FOOTBALL TEAM
    @PostMapping("/add-team")
    public ResponseEntity<Integer> addFootballTeam(@Valid @RequestBody FootballTeamDTO footballTeamDTO) {
        return new ResponseEntity<>(tournamentService.addTeamJoinTournament(footballTeamDTO), HttpStatus.OK);
    }

    @GetMapping("/all-team")
    public ResponseEntity<List<FootballTeamDTO>> displayAllTeam() {
        return new ResponseEntity<>(tournamentService.getAllFootballTeam(), HttpStatus.OK);
    }
}
