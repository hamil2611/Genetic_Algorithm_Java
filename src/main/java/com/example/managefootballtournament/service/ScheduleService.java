package com.example.managefootballtournament.service;

import com.example.managefootballtournament.domain.ScheduleDTO;
import com.example.managefootballtournament.entity.Match;
import com.example.managefootballtournament.entity.Schedule;
import com.example.managefootballtournament.entity.ScheduleInDay;
import com.example.managefootballtournament.entity.Tournament;
import com.example.managefootballtournament.geneticAlgorithm.GeneticAlgorithm;
import com.example.managefootballtournament.geneticAlgorithm.InitCoupleMatch;
import com.example.managefootballtournament.geneticAlgorithm.Population;
import com.example.managefootballtournament.geneticAlgorithm.ScheduleGA;
import com.example.managefootballtournament.geneticAlgorithm.entityGA.MatchGA;
import com.example.managefootballtournament.geneticAlgorithm.entityGA.RoundGA;
import com.example.managefootballtournament.geneticAlgorithm.test.SolveGAv2;
import com.example.managefootballtournament.repository.ScheduleRepository;
import com.example.managefootballtournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ModelMapper modelMapper;
    private final TournamentRepository tournamentRepository;
    private final SolveGAv2 solveGAv2;
    private final GeneticAlgorithm geneticAlgorithm;
    private final InitCoupleMatch initCoupleMatch;

    public Integer addSchedule(ScheduleInDay scheduleInDay) {
        scheduleRepository.save(scheduleInDay);
        return 1;
    }

    public List<ScheduleDTO> findScheduleForTournament(Integer tournamentId) {
        return scheduleRepository.findAllByTournamentId(tournamentId).stream().map(x -> modelMapper.map(x, ScheduleDTO.class)).collect(Collectors.toList());
    }

    public List<ScheduleInDay> solveSchedule(Integer tournamentId, Integer amountIndividual, Integer mutationProbability) {
        List<ScheduleInDay> listScheduleInDay = new ArrayList<>();
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        Tournament tournament = tournamentOptional.get();
        initCoupleMatch.initMatch(tournament);
        int amountMatchInRounds = tournament.getReferees().size();
        int amountStadiums = tournament.getStadiums().size();
//        int amountMatchInRounds = tournament.getFootballTeams().size()/2;
//        int amountStadiums = tournament.getAmountDayInRound() * tournament.getTimeSlots().size();
        int amountRounds = 0;
        //Validate Input Amount Round
        if (tournament.getFootballTeams().size() % 2 != 0)
            amountRounds = tournament.getFootballTeams().size();
        else
            amountRounds = tournament.getFootballTeams().size() - 1;

        List<RoundGA> listRoundGA = solveGAv2.methodSolveGA(amountIndividual, amountMatchInRounds, amountStadiums, amountMatchInRounds, amountStadiums, amountRounds, tournament.getTimeSlots().size());
        int numberOfRound = 1;
        for (RoundGA vd : listRoundGA) {
            List<Match> listMatch = getRound(tournament.getMatchs(), numberOfRound);
            for (MatchGA td : vd.getListMatchGA()) {
                ScheduleInDay scheduleInDay = new ScheduleInDay();
                scheduleInDay.setRound(numberOfRound);
                scheduleInDay.setDay((numberOfRound - 1) * tournament.getAmountDayInRound() + calcuMatchDay(td.getTimeslot(), tournament.getTimeSlots().size()) + 1);
                scheduleInDay.setDayInRound(tournament.getAmountDayInRound());
                scheduleInDay.setIndexTimeSlot((td.getTimeslot() + tournament.getTimeSlots().size() - 1) % tournament.getTimeSlots().size());
                scheduleInDay.setTimeSlot(tournament.getTimeSlots().get((td.getTimeslot() + tournament.getTimeSlots().size() - 1) % tournament.getTimeSlots().size()).getSlot());
                scheduleInDay.setNumberOfMatch(td.getMaTD());
                scheduleInDay.setNameMatch(listMatch.get(td.getMaTD()).getCoupleMatch());
                scheduleInDay.setNameReferee(tournament.getReferees().get(td.getTrongtai()).getName());
                scheduleInDay.setNameStadium(tournament.getStadiums().get(td.getSanvandong()).getName());

                scheduleInDay.setTournament(tournament);
                listScheduleInDay.add(scheduleInDay);
            }
            numberOfRound++;
        }
//        scheduleRepository.saveAll(listSchedule);
        return listScheduleInDay;
    }

    public List<Schedule> initPopulationState(Integer tournamentId, Integer amountIndividual) {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        Tournament tournament = tournamentOptional.get();
        initCoupleMatch.initMatch(tournament);
        int amountReferee = tournament.getReferees().size();
        int amountStadium = tournament.getStadiums().size();
        int amountMatchInRounds = tournament.getFootballTeams().size() / 2;
        int amountTimeslot = tournament.getAmountDayInRound() * tournament.getTimeSlots().size();
        int amountRounds = 0;
        //Validate Input Amount Round
        if (tournament.getFootballTeams().size() % 2 != 0)
            amountRounds = tournament.getFootballTeams().size();
        else
            amountRounds = tournament.getFootballTeams().size() - 1;
        List<Schedule> schedules = new ArrayList<>();

        Population population = geneticAlgorithm.initRanDomQuanThe(amountIndividual, amountReferee, amountStadium, amountMatchInRounds, amountTimeslot, amountRounds);
        population.getListScheduleGAGA().forEach(x -> {
            schedules.add(convertToSchedule(x,tournament));
        });
        return schedules;
    }

    public Schedule convertToSchedule(ScheduleGA scheduleGA, Tournament tournament) {
        List<ScheduleInDay> scheduleInDayList = new ArrayList<>();

        int amountReferee = tournament.getReferees().size();
        int amountStadium = tournament.getStadiums().size();
        int amountMatchInRoundInRound = tournament.getFootballTeams().size() / 2;
        int amountTimeslot = tournament.getAmountDayInRound() * tournament.getTimeSlots().size();
        int amountRound = 0;
        int timeslotInDay = tournament.getTimeSlots().size();
        //Validate Input Amount Round
        if (tournament.getFootballTeams().size() % 2 != 0)
            amountRound = tournament.getFootballTeams().size();
        else
            amountRound = tournament.getFootballTeams().size() - 1;
        int[] chromosome = scheduleGA.getChromosome();
        int day=0;
        for (int r = 0; r < amountRound; r++) {
            for (int m = 0; m < amountMatchInRoundInRound; m++) {
                ScheduleInDay scheduleInDay = new ScheduleInDay();
                int round = r+1;
                day++;
                int indexTimeslot = chromosome[r * amountMatchInRoundInRound + m]%timeslotInDay;
                String nameTimeslot = tournament.getTimeSlots().get(indexTimeslot).getSlot();
                String nameReferee = tournament.getReferees().get(chromosome[amountMatchInRoundInRound * amountRound + r * amountMatchInRoundInRound + m]).getName();
                String nameStadium = tournament.getStadiums().get(chromosome[amountMatchInRoundInRound * amountRound * 2 + r * amountMatchInRoundInRound + m]).getName();
                String nameMatch = tournament.getMatchs().get(r*amountMatchInRoundInRound+m).getCoupleMatch();
                scheduleInDay.setDay(day);
                scheduleInDay.setRound(round);
                scheduleInDay.setAmountTimeslot(amountTimeslot);
                scheduleInDay.setIndexTimeSlot(indexTimeslot);
                scheduleInDay.setTimeSlot(nameTimeslot);
                scheduleInDay.setNameReferee(nameReferee);
                scheduleInDay.setNameStadium(nameStadium);
                scheduleInDay.setNameMatch(nameMatch);
                scheduleInDayList.add(scheduleInDay);
            }
        }
        Schedule schedule = new Schedule(scheduleInDayList);
        return schedule;
    }

    public List<Match> getRound(List<Match> listMatchInit, int numberOfRound) {
        List<Match> listMatch = new ArrayList<>();
        for (Match match : listMatchInit) {
            if (match.getRound() == numberOfRound)
                listMatch.add(match);
        }
        return listMatch;
    }

    public int calcuMatchDay(int maTimeSlot, int amountTimeslotInDay) {
        if (maTimeSlot % amountTimeslotInDay != 0)
            return maTimeSlot / amountTimeslotInDay;
        else
            return maTimeSlot / amountTimeslotInDay - 1;
    }
}