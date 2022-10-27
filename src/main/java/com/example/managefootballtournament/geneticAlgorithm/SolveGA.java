package com.example.managefootballtournament.geneticAlgorithm;

import java.util.Random;

public class SolveGA {
    public int[][][] initRanDomQuanThe(int slCaThe, int slTrongTai, int slSVD, int slTranDau, int slTimeSlot) {
        int CaThe[][][] = new int[slCaThe][slTranDau][3];
        Random random = new Random();
        for (int i = 0; i < slCaThe; i++) {
            for (int k = 0; k < slTranDau; k++)
                for (int j = 0; j < 3; j++) {
                    if (j == 0)
                        CaThe[i][k][j] = random.nextInt(slTimeSlot) + 1;
                    if (j == 1)
                        CaThe[i][k][j] = random.nextInt(slTrongTai);
                    if (j == 2)
                        CaThe[i][k][j] = random.nextInt(slSVD);
                }

            //Sort Array tang dan
            for (int k = 0; k < slTranDau; k++)
                for (int j = 0; j < slTranDau - k - 1; j++)
                    if (CaThe[i][j][0] > CaThe[i][j + 1][0]) {
                        int temp = CaThe[i][j][0];
                        CaThe[i][j][0] = CaThe[i][j + 1][0];
                        CaThe[i][j + 1][0] = temp;
                    }
        }
        return CaThe;
    }

    public boolean checkExceptionSVDandTrongTai(int array[], int slTrongTai, int slSVD) {
        int arrCount[] = new int[array.length];
        for (int i = 0; i < array.length; i++)
            arrCount[i] = -1;
        for (int i = 0; i < array.length; i++) {
            int count = 1;
            for (int j = i + 1; j < arrCount.length; j++) {
                if (array[i] == array[j]) {
                    count++;
                    arrCount[j] = 0;
                }
            }
            if (arrCount[i] != 0) arrCount[i] = count;
        }
        for (int i = 0; i < array.length; i++) {
            if (arrCount[i] > slTrongTai || arrCount[i] > slSVD)
                return false;
        }
        return true;
    }

    public void calcuDoThichNghi(int QuanThe[][][], int slCaThe, int slTrongTai,
                                 int slSVD, int slTranDau, int slTimeSlot) {

        for (int i = 0; i < slCaThe; i++) {
            int doThichNghi = 0;
            int matchInCaThe[] = new int[slTranDau];
            System.out.println("Ca the " + i);
            for (int k = 0; k < slTranDau; k++) {
                matchInCaThe[k] = QuanThe[i][k][0];
            }
            if (!checkExceptionSVDandTrongTai(matchInCaThe, slTrongTai, slSVD))
                doThichNghi += TrongSoRangBuoc.RANG_BUOC_TRONG_TAI;
        }
    }

    public int[][][] methodLaiGhep(int[][][] QuanTheCu, int index, int slCaThe, int slTrongTai, int slSVD, int slTranDau, int slTimeSlot) {
        if (index >= slTranDau)
            return QuanTheCu;
        int QuanTheMoi[][][] = new int[slCaThe][slTranDau][3];
        for (int i = 0; i < QuanTheCu.length; i ++) {
            if (i%2 ==1 || i==QuanTheCu.length-1)
                continue;
            for (int k = 0; k < slTranDau; k++) {
                if (k < index) {
                    QuanTheMoi[i][k][0] = QuanTheCu[i][k][0];
                    QuanTheMoi[i][k][1] = QuanTheCu[i][k][1];
                    QuanTheMoi[i][k][2] = QuanTheCu[i][k][2];
                } else {
                    QuanTheMoi[i][k][0] = QuanTheCu[i + 1][k][0];
                    QuanTheMoi[i][k][1] = QuanTheCu[i + 1][k][1];
                    QuanTheMoi[i][k][2] = QuanTheCu[i + 1][k][2];
                }
            }
            for (int k = 0; k < slTranDau; k++) {
                if (k < index) {
                    QuanTheMoi[i + 1][k][0] = QuanTheCu[i + 1][k][0];
                    QuanTheMoi[i + 1][k][1] = QuanTheCu[i + 1][k][1];
                    QuanTheMoi[i + 1][k][2] = QuanTheCu[i + 1][k][2];
                } else {
                    QuanTheMoi[i + 1][k][0] = QuanTheCu[i][k][0];
                    QuanTheMoi[i + 1][k][1] = QuanTheCu[i][k][1];
                    QuanTheMoi[i + 1][k][2] = QuanTheCu[i][k][2];
                }
            }
        }

        return QuanTheMoi;
    }

    public static void main(String[] args) {
        SolveGA solveGA = new SolveGA();

        int slCaThe = 500;
        int slTrongTai = 2;
        int slSVD = 2;
        int slTranDau = 5;
        int slTimeSlot = 21;

        int QuanThe[][][] = solveGA.initRanDomQuanThe(slCaThe, slTrongTai, slSVD, slTranDau, slTimeSlot);
        int QuanTheMoi[][][] = solveGA.methodLaiGhep(QuanThe, 3, slCaThe, slTrongTai, slSVD, slTranDau, slTimeSlot);
        solveGA.calcuDoThichNghi(QuanThe, slCaThe, slTrongTai, slSVD, slTranDau, slTimeSlot);


        for (int i = 0; i < slCaThe; i++) {
            System.out.println("Ca the " + (i + 1) + ":");
            for (int k = 0; k < 5; k++){
                System.out.print(QuanThe[i][k][0] + " " + QuanThe[i][k][1] + " " + QuanThe[i][k][2] + ":");
                System.out.println(QuanTheMoi[i][k][0] + " " + QuanTheMoi[i][k][1] + " " + QuanTheMoi[i][k][2]);
            }
        }
    }
}
