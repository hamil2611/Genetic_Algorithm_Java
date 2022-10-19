package com.example.managefootballtournament.service;

import com.example.managefootballtournament.domain.ScheduleDTO;
import com.example.managefootballtournament.entity.Schedule;
import com.example.managefootballtournament.repository.ScheduleRepository;
import com.example.managefootballtournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ModelMapper modelMapper;

    public Integer addSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
        return 1;
    }

    public List<ScheduleDTO> findScheduleForTournament(Integer tournamentId) {
        return scheduleRepository.findAllByTournamentId(tournamentId).stream().map(x -> modelMapper.map(x, ScheduleDTO.class)).collect(Collectors.toList());
    }
}
