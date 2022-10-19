package com.example.managefootballtournament;

import com.example.managefootballtournament.geneticAlgorithm.InitCoupleMatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManageFootballTournamentApplicationTests {

    @Autowired
    InitCoupleMatch initCoupleMatch;
    @Test
    void contextLoads() {
        initCoupleMatch.solveMatchCouple(6);
    }

}
