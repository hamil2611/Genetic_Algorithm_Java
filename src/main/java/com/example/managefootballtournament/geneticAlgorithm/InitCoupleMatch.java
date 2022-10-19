package com.example.managefootballtournament.geneticAlgorithm;

import com.example.managefootballtournament.entity.FootballTeam;
import com.example.managefootballtournament.entity.Match;
import com.example.managefootballtournament.repository.FootballTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class InitCoupleMatch {
    private final FootballTeamRepository footballTeamRepository;
    private final ChinhHop chinhHop;
    public List<Match> solveMatchCouple(int tournamentId){
        List<Match> matchList = new ArrayList<>();
        List<FootballTeam> footballTeamList = footballTeamRepository.findAllFootballTeamOfTournament(tournamentId);
        Map<Integer, FootballTeam> footballTeamMap= new HashMap<>();
        if(footballTeamList.size()%2!=0)
            footballTeamMap.put(0,new FootballTeam());
        for(int i=1; i<=footballTeamList.size(); i++ ){
                footballTeamMap.put(i,footballTeamList.get(i-1));
        }
        int amountRound = footballTeamList.size()*(footballTeamList.size()-1);
//        for (int i=1; i<=amountRound; i++){
//            for(Map.Entry<Integer, FootballTeam> map: footballTeamMap.entrySet()){
//                System.out.println(map.getKey()+"-----"+map.getValue().getNameTeam());
//                Match match = new Match();
//                match.setRound(i);
//                match.setCoupleMatch();
//            }
//        }
        chinhHop.mainSolve(footballTeamList,footballTeamList.size());
        return matchList;
    }
}
