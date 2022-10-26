package com.example.managefootballtournament.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "referee")
@Data
@NoArgsConstructor
public class Referee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "tournament_hold_id")
    private Tournament tournament;
}
