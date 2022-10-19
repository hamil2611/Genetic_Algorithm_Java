package com.example.managefootballtournament.domain;

import com.example.managefootballtournament.entity.Tournament;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FootballTeamDTO {
    @JsonProperty
    private int id;
    @JsonProperty
    private String nameTeam;
    @JsonProperty
    private String phoneNumber;
    @JsonProperty
    private String address;
    @JsonProperty
    private int amountPlayer;
    @JsonProperty
    private Tournament tournament;
}
