package com.example.managefootballtournament.controller;

import com.example.managefootballtournament.domain.ScheduleDTO;
import com.example.managefootballtournament.entity.Schedule;
import com.example.managefootballtournament.geneticAlgorithm.InitCoupleMatch;
import com.example.managefootballtournament.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
     private final ScheduleService scheduleService;


     @PostMapping("/add-schedule")
     public ResponseEntity<Integer> addSchedule(@RequestBody Schedule schedule){
          return new ResponseEntity<>(scheduleService.addSchedule(schedule), HttpStatus.OK);
     }

     @GetMapping("/detail-tournament")
     public ResponseEntity<List<ScheduleDTO>> displayDetailScheduleOfTournament(@RequestParam(required = false) int tournamentId){
          return new ResponseEntity<>(scheduleService.findScheduleForTournament(tournamentId), HttpStatus.OK);
     }


     /////
     private final InitCoupleMatch initCoupleMatch;
     @PostMapping("/solve-match")
     public ResponseEntity displayMatch(@RequestParam(required = false) int tournamentId){
          return new ResponseEntity<>(initCoupleMatch.solveMatchCouple(tournamentId), HttpStatus.OK);
     }
}

