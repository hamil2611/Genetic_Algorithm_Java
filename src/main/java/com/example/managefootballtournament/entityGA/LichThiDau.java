package com.example.managefootballtournament.entityGA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LichThiDau {
    private List<VongDau> listVongDau;
}
