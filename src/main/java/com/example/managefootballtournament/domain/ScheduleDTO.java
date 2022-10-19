package com.example.managefootballtournament.domain;

import com.example.managefootballtournament.entity.Tournament;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ScheduleDTO {
    @JsonProperty
    private int id;
    @JsonProperty
    private LocalDate dateHappen;
    @JsonProperty
    private String timeSlot;
    @JsonProperty
    private LocalDateTime startTime;
    @JsonProperty
    private LocalDateTime endTime;
    @JsonProperty
    private Tournament tournament;
}
