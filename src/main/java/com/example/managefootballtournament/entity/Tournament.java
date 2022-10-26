package com.example.managefootballtournament.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tournament")
@Getter
@Setter
@NoArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name_tournament")
    private String nameTournament;
    @Column(name = "amount_stadium")
    private int amountStadium;
    @Column(name = "amount_referee")
    private int amountReferee;
    @Column(name = "amount_match")
    private int amountMatch;
    @Column(name = "amount_day_in_round")
    private int amountDayInRound;
    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FootballTeam> footballTeams;

    @OneToMany(mappedBy = "tournament",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Schedule> schedule;

    @OneToMany(mappedBy = "tournament",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TimeSlot> timeSlots;

    @OneToMany(mappedBy = "tournament",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Match> matchs;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Referee> referees;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Stadium> stadiums;

}
