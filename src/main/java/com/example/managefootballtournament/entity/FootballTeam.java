package com.example.managefootballtournament.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "football_team")
@Getter
@Setter
@NoArgsConstructor
public class FootballTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name_team")
    private String nameTeam;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Column(name = "amount_player")
    private int amountPlayer;
    @ManyToOne
    @JoinColumn(name = "tournament_hold_id")
    private Tournament tournament;
}
