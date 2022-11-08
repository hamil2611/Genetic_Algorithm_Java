package com.example.managefootballtournament.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Schedule {
    private List<ScheduleInDay> scheduleInDayList;
}
