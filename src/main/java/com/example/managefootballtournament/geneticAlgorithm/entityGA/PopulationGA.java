package com.example.managefootballtournament.geneticAlgorithm.entityGA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopulationGA {
    private List<ScheduleGAv1> listScheduleGAv1;
}
