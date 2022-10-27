package com.example.managefootballtournament.geneticAlgorithm.test;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public List<Integer> initListMatchInt(int slDoiBong) {
        List<java.lang.Integer> listMatchInt = new ArrayList<>();
        for (int i = 0; i < slDoiBong; i++)
            if (slDoiBong % 2 == 0)
                listMatchInt.add(i + 1);
            else
                listMatchInt.add(i);
        return listMatchInt;
    }

    public static void main(String[] args) {
        Main main = new Main();
        int amountTeam = 5;
        int loopAmountTeam = 0;
        List<Integer> listMatchInt = main.initListMatchInt(amountTeam);
        if(amountTeam%2!=0)
            loopAmountTeam = amountTeam;
        else
            loopAmountTeam = amountTeam - 1;
        for (int v = 1; v <= amountTeam ; v++) {
            //Số đội bóng tham gia lẻ
            if (amountTeam % 2 != 0) {
                loopAmountTeam = amountTeam - 1;
                for (int i = 0; i < loopAmountTeam; i += 2)
                    System.out.println(listMatchInt.get(i) + " " + listMatchInt.get(i + 1));
                int tmp = listMatchInt.get(0);
                listMatchInt.remove(0);
                listMatchInt.add(listMatchInt.size(), tmp);
            }
            //Số đội bóng tham gia chẵn
            else {
                loopAmountTeam = amountTeam;
                for (int i = 0; i < loopAmountTeam; i += 2)
                    System.out.println(listMatchInt.get(i) + " " + listMatchInt.get(i + 1));
                int tmp = listMatchInt.get(listMatchInt.size() - 1);
                listMatchInt.remove(listMatchInt.size() - 1);
                listMatchInt.add(1, tmp);
            }
            System.out.println("--------------");
        }
    }
}
