package com.example.managefootballtournament.service;

import com.example.managefootballtournament.repository.FootballTeamRepository;
import com.example.managefootballtournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TournamentRepository tournamentRepository;
    private final FootballTeamRepository footballTeamRepository;
    private final ModelMapper modelMapper;

//    @Value("${validation.mail.notEmpty}")
//    private String value ;
//    public Integer addNewTournament(TournamentDTO tournamentDTO) {
//        tournamentRepository.save(modelMapper.map(tournamentDTO, Tournament.class));
//        logger.info("----- Add Successfully Tournament: {} ", tournamentDTO.getNameTournament());
//        return 1;
//    }
//
//    public Integer addTeamJoinTournament(FootballTeamDTO footballTeamDTO) {
//        footballTeamRepository.save(modelMapper.map(footballTeamDTO, FootballTeam.class));
//        logger.info("----- Add Successfully Team: {} Join Tournament: {}",footballTeamDTO.getNameTeam(),footballTeamDTO.getTournament().getNameTournament());
//        return 1;
//    }
//
//    public List<TournamentDTO> getAllTournament() {
//        return tournamentRepository.findAll().stream().map(x -> modelMapper.map(x,TournamentDTO.class)).collect(Collectors.toList());
//    }
//
//    public List<FootballTeamDTO> getAllFootballTeam() {
//        return footballTeamRepository.findAll().stream().map(x -> modelMapper.map(x,FootballTeamDTO.class)).collect(Collectors.toList());
//    }



}
