package com.example.managefootballtournament.controller;

import com.example.managefootballtournament.entity.Schedule;
import com.example.managefootballtournament.entity.ScheduleInDay;
import com.example.managefootballtournament.geneticAlgorithm.GeneticAlgorithm;
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
    private final GeneticAlgorithm geneticAlgorithm;

//    @GetMapping
//    public ResponseEntity<List<ScheduleInDay>> getSchedule(@RequestParam(required = false) Integer tournamentId,
//                                                           @RequestParam(required = false) Integer amountIndividual,
//                                                           @RequestParam(required = false) Integer mutationProbability) {
//        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.solveSchedule(tournamentId, amountIndividual,mutationProbability));
//    }

//    @PostMapping("/add-schedule")
//    public ResponseEntity<Integer> addSchedule(@RequestBody ScheduleInDay scheduleInDay) {
//        return new ResponseEntity<>(scheduleService.addSchedule(scheduleInDay), HttpStatus.OK);
//    }
//
//    @GetMapping("/detail-tournament")
//    public ResponseEntity<List<ScheduleDTO>> displayDetailScheduleOfTournament(@RequestParam(required = false) int tournamentId) {
//        return new ResponseEntity<>(scheduleService.findScheduleForTournament(tournamentId), HttpStatus.OK);
//    }
//
//    @PostMapping("/tool-schedule")
//    public ResponseEntity solveSchedule(@RequestParam(required = false) int tournamentId) {
//        return ResponseEntity.ok("");
//    }

    @GetMapping("/init")
    public ResponseEntity<List<Schedule>> initPopulation(@RequestParam(required = false) Integer tournamentId,
                                                          @RequestParam(required = false) Integer amountIndividual,
                                                          @RequestParam(required = false) Integer mutationProbability,
                                                          @RequestParam(required = false) boolean crossover){
        System.out.println("INIT");
        return new ResponseEntity<>(scheduleService.initPopulationState(tournamentId,amountIndividual, mutationProbability,crossover),HttpStatus.OK);
    }

    @GetMapping("/schedule/{id}")
    private  ResponseEntity displaySchedule(@PathVariable int id){
        return new ResponseEntity<>(scheduleService.getSchedule(id),HttpStatus.OK);
    }
}

