package com.example.managefootballtournament.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "timeslot")
@Data
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "slot")
    private String slot;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tournament_hold_id")
    private Tournament tournament;
}
