package com.example.managefootballtournament.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "schedule_in_day")
@Data
public class ScheduleInDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "index")
    private int index;
    @Column(name = "round")
    private int round;
    @Column(name = "day")
    private int day;
    @Column(name = "day_in_round")
    private int dayInRound;
    @Column(name = "number_of_match")
    private int numberOfMatch;
    @Column(name = "amount_time_slot")
    private int amountTimeslot;
    @Column(name = "amount_time_slot_in_day")
    private int amountTimeslotInDay;
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

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id")
    @JsonIgnore
    private Schedule schedule;

}
