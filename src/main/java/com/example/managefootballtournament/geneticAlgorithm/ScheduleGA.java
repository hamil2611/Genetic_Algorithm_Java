package com.example.managefootballtournament.geneticAlgorithm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleGA {
    private int[] chromosome;
    private float fitness;
    private int totalConstraintClashReferee;
    private int totalConstraintRefereeInDay;
    private int totalConstraintSpaceInRound;
    private int totalConstraintSpaceInDay;
}

