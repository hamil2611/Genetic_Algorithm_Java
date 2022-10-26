package com.example.managefootballtournament.service;

import com.example.managefootballtournament.domain.ScheduleDTO;
import com.example.managefootballtournament.entity.Match;
import com.example.managefootballtournament.entity.Schedule;
import com.example.managefootballtournament.entity.Tournament;
import com.example.managefootballtournament.entityGA.TranDau;
import com.example.managefootballtournament.entityGA.VongDau;
import com.example.managefootballtournament.geneticAlgorithm.SolveGAv2;
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

    public Integer addSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
        return 1;
    }

    public List<ScheduleDTO> findScheduleForTournament(Integer tournamentId) {
        return scheduleRepository.findAllByTournamentId(tournamentId).stream().map(x -> modelMapper.map(x, ScheduleDTO.class)).collect(Collectors.toList());
    }

    public List<Schedule> solveSchedule(Integer tournamentId) {
        List<Schedule> listSchedule = new ArrayList<>();
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        Tournament tournament = tournamentOptional.get();

        int slTrongTai = tournament.getAmountReferee();
        int slSVD = tournament.getAmountStadium();
        int slTranDau = 0;
        int slTimeSlot = tournament.getAmountDayInRound() * tournament.getTimeSlots().size();
        int slVongDau = slTranDau * 2 - 1;
        List<VongDau> listVongDau = solveGAv2.methodSolveGA(10, slTrongTai, slSVD, slTranDau, slTimeSlot, slVongDau);

        int numberOfRound = 1;
        int ngaythidau = 1;
        for (VongDau vd : listVongDau) {

            List<Match> listMatch = getRound(tournament.getMatchs(), numberOfRound);
            numberOfRound++;
            for (TranDau td : vd.getListTranDau()) {
                Schedule schedule = new Schedule();
                schedule.setDay(ngaythidau);
                ngaythidau++;
                schedule.setTournament(tournament);
                schedule.setTimeSlot(tournament.getTimeSlots().get(td.getTimeslot()).getSlot());
                schedule.setNumberOfMatch(td.getMaTD());
                schedule.setNameMatch(listMatch.get(td.getMaTD() - 1).getCoupleMatch());
                schedule.setTimeSlot(tournament.getTimeSlots().get(td.getTimeslot()).getSlot());
                schedule.setNameReferee(tournament.getReferees().get(td.getTrongtai()).getName());
                schedule.setNameStadium(tournament.getReferees().get(td.getTimeslot()).getName());
                listSchedule.add(schedule);
            }
        }
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
}