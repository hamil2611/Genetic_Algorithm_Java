package com.example.managefootballtournament.entityGA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class VongDau {
    private List<TranDau> listTranDau;
}
