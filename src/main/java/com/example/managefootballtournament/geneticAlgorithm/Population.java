package com.example.managefootballtournament.geneticAlgorithm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Population {
    private List<ScheduleGA> listScheduleGAGA;
}
