package com.example.managefootballtournament.service;

import com.example.managefootballtournament.entity.Schedule;
import com.example.managefootballtournament.entity.ScheduleInDay;
import com.example.managefootballtournament.entity.Tournament;
import com.example.managefootballtournament.geneticAlgorithm.GeneticAlgorithm;
import com.example.managefootballtournament.geneticAlgorithm.InitCoupleMatch;
import com.example.managefootballtournament.geneticAlgorithm.Population;
import com.example.managefootballtournament.geneticAlgorithm.ScheduleGA;
import com.example.managefootballtournament.repository.ScheduleInDayRepository;
import com.example.managefootballtournament.repository.ScheduleRepository;
import com.example.managefootballtournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleInDayRepository scheduleInDayRepository;
    private final TournamentRepository tournamentRepository;
    private final GeneticAlgorithm geneticAlgorithm;
    private final InitCoupleMatch initCoupleMatch;

    public List<Schedule> initPopulationState(Integer tournamentId, Integer amountIndividual, Integer mutationProbability, Integer crossoverProbability, boolean crossover) {
        final long startTime = System.currentTimeMillis();

        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        Tournament tournament = tournamentOptional.get();
//        initCoupleMatch.initMatch(tournament);
        int amountReferee = tournament.getReferees().size();
        int amountMatchInRounds = tournament.getFootballTeams().size() / 2;
        int amountTimeslot = 3 * tournament.getTimeSlots().size();
        int amountTimeslotInDay = tournament.getTimeSlots().size();
        int amountRounds = 0;
        //Validate Input Amount Round
        if (tournament.getFootballTeams().size() % 2 != 0)
            amountRounds = tournament.getFootballTeams().size();
        else
            amountRounds = tournament.getFootballTeams().size() - 1;
        System.out.println(amountReferee+" "+ amountMatchInRounds + " "+amountTimeslot+" " +amountTimeslotInDay + " "+ amountRounds );
        List<Schedule> schedules = new ArrayList<>();

        Population population = geneticAlgorithm.initPopulation(amountIndividual, amountReferee, amountMatchInRounds, amountTimeslot, amountRounds);
        population.setListScheduleGAGA(geneticAlgorithm.calcuFitness(population, amountRounds, amountMatchInRounds, amountTimeslotInDay).getListScheduleGAGA());
        if (crossover) {
            List<ScheduleGA> scheduleGAS = crossoverState(population, amountRounds, amountMatchInRounds, amountTimeslot, amountReferee,amountTimeslotInDay
                    , mutationProbability,crossoverProbability );
            final long endTime = System.currentTimeMillis();
            for (int i = 0; i < scheduleGAS.size(); i++) {
                Schedule schedule = convertToSchedule(scheduleGAS.get(i), tournament);
                schedules.add(schedule);
            }
            Schedule schedule = schedules.get(0);
            schedule.setRunTime(convertToMinutes(endTime-startTime));
            schedules.remove(0);
            schedules.add(0,schedule);
            scheduleRepository.saveAll(schedules);
            return schedules;
        }
        List<ScheduleGA> scheduleGAS = population.getListScheduleGAGA();
        for (int i = 0; i < scheduleGAS.size(); i++) {
            Schedule schedule = convertToSchedule(scheduleGAS.get(i), tournament);
            schedules.add(schedule);
        }
        return schedules;
    }

    public List<ScheduleGA> crossoverState(Population population, int amountRound, int amountMatchInRound,
                                           int amountTimeslot, int amountReferee, int amountTimeslotInDay, int mutationProbability,int crossoverProbability ) {
        Population crossoverPopulation = geneticAlgorithm.crossoverPopulation(population, amountRound, amountMatchInRound,
                amountTimeslot, amountReferee, mutationProbability , crossoverProbability);
        float fitnessBest = population.getListScheduleGAGA().get(0).getFitness();
        int count = 0;
        double checkpoint =1L;
        while (fitnessBest < checkpoint) {
            count++;
            crossoverPopulation = geneticAlgorithm.crossoverPopulation(population, amountRound, amountMatchInRound, amountTimeslot, amountReferee, mutationProbability, crossoverProbability);
            crossoverPopulation.setListScheduleGAGA(geneticAlgorithm.calcuFitness(crossoverPopulation, amountRound,
                    amountMatchInRound, amountTimeslotInDay).getListScheduleGAGA());
            fitnessBest = crossoverPopulation.getListScheduleGAGA().get(0).getFitness();
            if (count % 100 == 0)
                System.out.println(count + ":" + fitnessBest);
//            if(count>=50000)
//                checkpoint=0.9;
        }
        ScheduleGA scheduleGA = crossoverPopulation.getListScheduleGAGA().get(0);
        scheduleGA.setAmountGeneration(count);
        crossoverPopulation.getListScheduleGAGA().remove(0);
        crossoverPopulation.getListScheduleGAGA().add(0,scheduleGA);
        System.out.println("fitnessBest: " + fitnessBest);
        System.out.println("generation: " + crossoverPopulation.getListScheduleGAGA().get(0).getAmountGeneration());
        return crossoverPopulation.getListScheduleGAGA();
    }

    public Schedule convertToSchedule(ScheduleGA scheduleGA, Tournament tournament) {
        List<ScheduleInDay> scheduleInDayList = new ArrayList<>();
        int amountMatchInRound = tournament.getFootballTeams().size()/2;
        int amountTimeslot = 4 * tournament.getTimeSlots().size();
        int amountRound = 0;
        int timeslotInDay = tournament.getTimeSlots().size();
        //Validate Input Amount Round
        if (tournament.getFootballTeams().size() % 2 != 0)
            amountRound = tournament.getFootballTeams().size();
        else
            amountRound = tournament.getFootballTeams().size() - 1;
        int[] chromosome = scheduleGA.getChromosome();
        int day = 0;
        Schedule schedule = new Schedule();
        for (int r = 0; r < amountRound; r++) {
            int index = 0;
            for (int m = 0; m < amountMatchInRound; m++) {
                index++;
                ScheduleInDay scheduleInDay = new ScheduleInDay();
                int round = r + 1;
                int indexTimeslot = chromosome[r * amountMatchInRound + m] % timeslotInDay;
                String nameTimeslot = tournament.getTimeSlots().get(indexTimeslot).getSlot();
                String nameReferee = tournament.getReferees().get(chromosome[amountMatchInRound * amountRound + r * amountMatchInRound + m]).getName();
                String nameStadium = "";
                String nameMatch = tournament.getMatchs().get(r * amountMatchInRound + m).getCoupleMatch();
                day = chromosome[r * amountMatchInRound + m] / timeslotInDay + 1;
                scheduleInDay.setIndex(index);
                scheduleInDay.setDay(day);
                scheduleInDay.setRound(round);
                scheduleInDay.setDayInRound(tournament.getAmountDayInRound());
                scheduleInDay.setAmountTimeslot(amountTimeslot);
                scheduleInDay.setAmountTimeslotInDay(tournament.getTimeSlots().size());
                scheduleInDay.setIndexTimeSlot(indexTimeslot);
                scheduleInDay.setTimeSlot(nameTimeslot);
                scheduleInDay.setNameReferee(nameReferee);
                scheduleInDay.setNameStadium(nameStadium);
                scheduleInDay.setNameMatch(nameMatch);
                scheduleInDay.setSchedule(schedule);
                scheduleInDayList.add(scheduleInDay);
            }
        }
        schedule.setFitness(scheduleGA.getFitness());
        schedule.setTotalConstraintClashReferee(scheduleGA.getTotalConstraintClashReferee());
        schedule.setTotalConstraintRefereeInDay(scheduleGA.getTotalConstraintRefereeInDay());
        schedule.setTotalConstraintSpaceInRound(scheduleGA.getTotalConstraintSpaceInRound());
        schedule.setTotalConstraintRefereeInDay(scheduleGA.getTotalConstraintRefereeInDay());
        schedule.setAmountGeneration(scheduleGA.getAmountGeneration());
        schedule.setScheduleInDayList(scheduleInDayList);
        return schedule;
    }
    public String convertToMinutes(Long timeMillis){
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeMillis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
        String minutesStr = String.valueOf(minutes) +":" +String.valueOf(seconds%60);
        return minutesStr;
    }
    public List<ScheduleInDay> getSchedule(int scheduleId){
        return scheduleInDayRepository.findByScheduleId(scheduleId);
    }


}