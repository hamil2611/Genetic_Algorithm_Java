package com.example.managefootballtournament.geneticAlgorithm;

import com.example.managefootballtournament.entity.FootballTeam;
import com.example.managefootballtournament.entity.Match;
import com.example.managefootballtournament.entity.Tournament;
import com.example.managefootballtournament.repository.FootballTeamRepository;
import com.example.managefootballtournament.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class InitCoupleMatch {
    private final FootballTeamRepository footballTeamRepository;
    private final MatchRepository matchRepository;

    public List<Integer> initListMatchInt(int slDoiBong) {
        List<Integer> listMatchInt = new ArrayList<>();
        for (int i = 0; i < slDoiBong; i++)
            if (slDoiBong % 2 == 0)
                listMatchInt.add(i + 1);
            else
                listMatchInt.add(i);
        return listMatchInt;
    }

    @Transactional
    public void initMatch(Tournament tournament) {
        int amountTeam = tournament.getFootballTeams().size();
        List<Match> listMatch = new ArrayList<>();
        List<FootballTeam> listFootBallTeam = footballTeamRepository.findAll();
        int loopAmountTeam = 0;
        List<Integer> listMatchInt = initListMatchInt(amountTeam);
        if (amountTeam % 2 != 0)
            loopAmountTeam = amountTeam;
        else
            loopAmountTeam = amountTeam - 1;
        for (int v = 1; v <= loopAmountTeam; v++) {
            //Số đội bóng tham gia lẻ
            if (amountTeam % 2 != 0) {
                for (int i = 0; i < amountTeam - 1; i += 2) {
                    String coupleMatch = listFootBallTeam.get(i).getNameTeam() + " VS " + listFootBallTeam.get(i + 1).getNameTeam();
                    Match match = new Match();
                    match.setCoupleMatch(coupleMatch);
                    match.setRound(v);
                    match.setTournament(tournament);
                    listMatch.add(match);
                }

                int tmp = listMatchInt.get(0);
                listMatchInt.remove(0);
                listMatchInt.add(listMatchInt.size(), tmp);
            }
            //Số đội bóng tham gia chẵn
            else {
                for (int i = 0; i < amountTeam; i += 2) {
                    String coupleMatch = listFootBallTeam.get(i).getNameTeam() + " VS " + listFootBallTeam.get(i + 1).getNameTeam();
                    Match match = new Match();
                    match.setCoupleMatch(coupleMatch);
                    match.setRound(v);
                    match.setTournament(tournament);
                    listMatch.add(match);
                }
                int tmp = listMatchInt.get(listMatchInt.size() - 1);
                listMatchInt.remove(listMatchInt.size() - 1);
                listMatchInt.add(1, tmp);
            }
        }
        matchRepository.saveAll(listMatch);
    }


}
