package com.example.managefootballtournament.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
public class TournamentDTO {
    @JsonProperty
    private int id;
    @JsonProperty
    private String nameTournament;
    @JsonProperty
    private String address;
    @JsonProperty
    private int amountStadium;
    @JsonProperty
    private int amountMatchInRound;
}
