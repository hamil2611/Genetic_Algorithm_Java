package com.example.managefootballtournament.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "schedule")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "fitness")
    private float fitness;
    @Column(name = "total_clash_referee")
    private int totalConstraintClashReferee;
    @Column(name = "total_referee_in_day")
    private int totalConstraintRefereeInDay;
    @Column(name = "total_space_in_round")
    private int totalConstraintSpaceInRound;
    @Column(name = "total_space_in_day")
    private int totalConstraintSpaceInDay;

    @OneToMany(mappedBy = "schedule",cascade = CascadeType.ALL)

    private List<ScheduleInDay> scheduleInDayList;
}
