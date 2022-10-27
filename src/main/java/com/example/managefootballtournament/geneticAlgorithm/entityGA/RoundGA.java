package com.example.managefootballtournament.geneticAlgorithm.entityGA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RoundGA {
    private List<MatchGA> listMatchGA;
}
