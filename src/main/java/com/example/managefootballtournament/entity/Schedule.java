package com.example.managefootballtournament.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "round")
    private int round;
    @Column(name = "day")
    private int day;
    @Column(name = "day_in_round")
    private int dayInRound;
    @Column(name = "number_of_match")
    private int numberOfMatch;
    @Column(name = "index_time_slot")
    private int indexTimeSlot;
    @Column(name = "time_slot")
    private String timeSlot;
    @Column(name = "name_match")
    private String nameMatch;
    @Column(name = "name_referee")
    private String nameReferee;
    @Column(name = "name_stadium")
    private String nameStadium;

    @Column(name = "date_happen")
    private String dateHappen;
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_hold_id")
    @JsonIgnore
    private Tournament tournament;

}
