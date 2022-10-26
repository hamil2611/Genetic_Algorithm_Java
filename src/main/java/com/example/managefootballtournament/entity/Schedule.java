package com.example.managefootballtournament.entity;

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
    @Column(name = "day")
    private int day;
    @Column(name = "number_of_match")
    private int numberOfMatch;
    @Column(name = "name_match")
    private String nameMatch;
    @Column(name = "name_referee")
    private String nameReferee;
    @Column(name = "name_stadium")
    private String nameStadium;
    @Column(name = "time_slot")
    private String timeSlot;
    @Column(name = "date_happen")
    private String dateHappen;
    @Column(name = "description")
    private String description;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tournament_hold_id")
    private Tournament tournament;

}