package com.example.managefootballtournament.service;

import com.example.managefootballtournament.domain.ScheduleDTO;
import com.example.managefootballtournament.entity.Match;
import com.example.managefootballtournament.entity.Schedule;
import com.example.managefootballtournament.entity.Tournament;
import com.example.managefootballtournament.geneticAlgorithm.InitCoupleMatch;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ModelMapper modelMapper;
    private final TournamentRepository tournamentRepository;
    private final SolveGAv2 solveGAv2;
    private final InitCoupleMatch initCoupleMatch;

    public Integer addSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
        return 1;
    }

    public List<ScheduleDTO> findScheduleForTournament(Integer tournamentId) {
        return scheduleRepository.findAllByTournamentId(tournamentId).stream().map(x -> modelMapper.map(x, ScheduleDTO.class)).collect(Collectors.toList());
    }

    public List<Schedule> solveSchedule(Integer tournamentId, Integer amountIndividual) {
        List<Schedule> listSchedule = new ArrayList<>();
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        Tournament tournament = tournamentOptional.get();
        initCoupleMatch.initMatch(tournament);
        int amountMatchs = tournament.getReferees().size();
        int amountStadiums = tournament.getStadiums().size();
//        int amountMatchs = tournament.getFootballTeams().size()/2;
//        int amountStadiums = tournament.getAmountDayInRound() * tournament.getTimeSlots().size();
        int amountRounds = 0;
        //Validate Input Amount Round
        if(tournament.getFootballTeams().size()%2!=0)
            amountRounds = tournament.getFootballTeams().size();
        else
            amountRounds = tournament.getFootballTeams().size()-1;

        List<RoundGA> listRoundGA = solveGAv2.methodSolveGA( amountIndividual, amountMatchs, amountStadiums, amountMatchs, amountStadiums, amountRounds, tournament.getTimeSlots().size());
        int numberOfRound = 1;
        for (RoundGA vd : listRoundGA) {
            List<Match> listMatch = getRound(tournament.getMatchs(), numberOfRound);
            for (MatchGA td : vd.getListMatchGA()) {
                Schedule schedule = new Schedule();
                schedule.setRound(numberOfRound);
                schedule.setDay((numberOfRound-1)*tournament.getAmountDayInRound() + calcuMatchDay(td.getTimeslot(),tournament.getTimeSlots().size())+1);
                schedule.setDayInRound(tournament.getAmountDayInRound());
                schedule.setIndexTimeSlot((td.getTimeslot() + tournament.getTimeSlots().size() -1)%tournament.getTimeSlots().size());
                schedule.setTimeSlot(tournament.getTimeSlots().get((td.getTimeslot() + tournament.getTimeSlots().size() -1)%tournament.getTimeSlots().size()).getSlot());
                schedule.setNumberOfMatch(td.getMaTD());
                schedule.setNameMatch(listMatch.get(td.getMaTD()).getCoupleMatch());
                schedule.setNameReferee(tournament.getReferees().get(td.getTrongtai()).getName());
                schedule.setNameStadium(tournament.getStadiums().get(td.getSanvandong()).getName());

                schedule.setTournament(tournament);
                listSchedule.add(schedule);
            }
            numberOfRound++;
        }
//        scheduleRepository.saveAll(listSchedule);
        return listSchedule;
    }

    public List<Match> getRound(List<Match> listMatchInit, int numberOfRound) {
        List<Match> listMatch = new ArrayList<>();
        for (Match match : listMatchInit) {
            if (match.getRound() == numberOfRound)
                listMatch.add(match);
        }
        return listMatch;
    }

    public int calcuMatchDay(int maTimeSlot, int amountTimeslotInDay){
        if(maTimeSlot%amountTimeslotInDay !=0)
            return maTimeSlot/amountTimeslotInDay;
        else
            return maTimeSlot/amountTimeslotInDay-1;
    }
}