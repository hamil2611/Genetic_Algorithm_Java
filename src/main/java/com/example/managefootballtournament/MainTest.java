package com.example.managefootballtournament;

import org.flywaydb.core.Flyway;

public class MainTest {
    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/football-tournament-v2", "postgres", "12345678")
                .load();
        flyway.migrate();
    }
}
