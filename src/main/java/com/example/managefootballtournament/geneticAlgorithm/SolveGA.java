package com.example.managefootballtournament.geneticAlgorithm;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SolveGA {
    private static int QuanThe[][][][];

    public void initRanDomQuanThe(int slCaThe, int slTrongTai, int slSVD, int slTranDau, int slTimeSlot, int slVongDau) {
        QuanThe = new int[slCaThe][slVongDau][slTranDau][3];
        Random random = new Random();
        for (int i = 0; i < slCaThe; i++) {
            for (int v = 0; v < slVongDau; v++)
                for (int k = 0; k < slTranDau; k++)
                    for (int j = 0; j < 3; j++) {
                        if (j == 0)
                            QuanThe[i][v][k][j] = random.nextInt(slTimeSlot) + 1;
                        if (j == 1)
                            QuanThe[i][v][k][j] = random.nextInt(slTrongTai);
                        if (j == 2)
                            QuanThe[i][v][k][j] = random.nextInt(slSVD);
                    }
            //{{{{12,1,1},{13,2,2},{5,...}}},{....Ca the tiep thep}}
            //T1TT2SVD2
            //Sort Array tang dan
            for (int v = 0; v < slVongDau; v++)
                for (int k = 0; k < slTranDau; k++)
                    for (int j = 0; j < slTranDau - k - 1; j++)
                        if (QuanThe[i][v][j][0] > QuanThe[i][v][j + 1][0]) {
                            for (int p = 0; p < 3; p++) {
                                int temp = QuanThe[i][v][j][p];
                                QuanThe[i][v][j][p] = QuanThe[i][v][j + 1][p];
                                QuanThe[i][v][j + 1][p] = temp;
                            }
                        }
        }
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


//    public void calcuDoThichNghi(int slCaThe, int slTrongTai,
//                                 int slSVD, int slTranDau, int slTimeSlot, int slVongDau) {
//        int arrDoTN[] = new int[slCaThe];
//        for (int i = 0; i < slCaThe; i++)
//            for (int v = 1; v <= slVongDau; v++) {
//                int doThichNghi = 0;
//                int matchInCaThe[] = new int[slTranDau];
//                for (int k = 0; k < slTranDau; k++) {
//                    matchInCaThe[k] = QuanThe[i][v][k][0];
//                }
//                if (!checkExceptionSVDandTrongTai(matchInCaThe, slTrongTai, slSVD))
//                    doThichNghi += TrongSoRangBuoc.RANG_BUOC_TRONG_TAI;
//                arrDoTN[i] = doThichNghi;
//            }
//        //Sort giam dan theo do thich nghi
//        for (int i = 0; i < slCaThe; i++)
//            for (int v = 1; v <= slVongDau; v++)
//                for (int j = 0; j < slCaThe - i - 1; j++)
//                    if (arrDoTN[j] < arrDoTN[j + 1]) {
//                        int tmp = arrDoTN[j];
//                        arrDoTN[j] = arrDoTN[j + 1];
//                        arrDoTN[j + 1] = tmp;
//                        for (int k = 0; k < slTranDau; k++) {
//                            for (int p = 0; p < 3; p++) {
//                                int temp = QuanThe[j][v][k][p];
//                                QuanThe[j][k][p] = QuanThe[j + 1][k][p];
//                                QuanThe[j + 1][k][p] = temp;
//                            }
//                        }
//                    }
//        //log test
//        for (int i = 0; i < slCaThe; i++) {
//            System.out.println("Do thich nghi:" + arrDoTN[i]);
//            for (int k = 0; k < slTranDau; k++)
//                System.out.println(QuanThe[i][k][0] + " " + QuanThe[i][k][1] + " " + QuanThe[i][k][2]);
//        }
//
//    }

    public int[][][][] methodLaiGhep(int[][][][] QuanTheCu, int index, int slCaThe,
                                   int slTrongTai, int slSVD, int slTranDau, int slTimeSlot,int slVongDau) {
        if (index >= slTranDau)
            return QuanTheCu;

        int QuanTheMoi[][][][] = new int[slCaThe][slVongDau][slTranDau][3];
        for (int i = 0; i < QuanTheCu.length; i++) {
            for(int v=0; v<slVongDau; v++){
                if (i % 2 == 1 || i == QuanTheCu.length - 1) {
                    if (i == QuanTheCu.length - 1)
                        for (int k = 0; k < slTranDau; k++) {
                            QuanTheMoi[i][v][k] = QuanTheCu[i][v][k];
                        }
                    continue;
                }
                for (int k = 0; k < slTranDau; k++) {
                    if (k < index) {
                        QuanTheMoi[i][v][k] = QuanTheCu[i][v][k];
                    } else {
                        QuanTheMoi[i][v][k] = QuanTheCu[i + 1][v][k];
                    }
                }
                for (int k = 0; k < slTranDau; k++) {
                    if (k < index) {
                        QuanTheMoi[i + 1][v][k] = QuanTheCu[i + 1][v][k];
                    } else {
                        QuanTheMoi[i + 1][v][k] = QuanTheCu[i][v][k];
                    }
                }
            }
        }
        return QuanTheMoi;
    }

    public int[][][][] Solve(int slCaThe, int slTrongTai, int slSVD, int slTranDau, int slTimeSlot, int slVongDau) {
        System.out.println("**************** Bước Khởi Tạo ****************");
        for (int i = 0; i < slCaThe; i++) {
            for (int k = 0; k < slTranDau; k++)
                System.out.println(QuanThe[i][k][0] + " " + QuanThe[i][k][1] + " " + QuanThe[i][k][2]);
            System.out.println("---------");
        }

//        //Chọn lọc
//        System.out.println("**************** Bước Sắp Xếp Chọn Lọc ****************");
//        calcuDoThichNghi(slCaThe, slTrongTai, slSVD, slTranDau, slTimeSlot, slVongDau);

        //Lai Ghép
        System.out.println("**************** Bước Lai Ghép ****************");
        int viTriLaiGhep = 3;
        int QuanTheMoi[][][][] = methodLaiGhep(QuanThe, viTriLaiGhep, slCaThe, slTrongTai, slSVD, slTranDau, slTimeSlot,slVongDau);
        for (int i = 0; i < slCaThe; i++) {
            System.out.println("Ca the " + (i + 1) + ":");
            for (int k = 0; k < 5; k++) {
                System.out.print(QuanThe[i][k][0] + " " + QuanThe[i][k][1] + " " + QuanThe[i][k][2] + ":");
                System.out.println(QuanTheMoi[i][k][0] + " " + QuanTheMoi[i][k][1] + " " + QuanTheMoi[i][k][2]);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SolveGA solveGA = new SolveGA();
        int slCaThe = 10;
        int slTrongTai = 2;
        int slSVD = 2;
        int slTranDau = 5;
        int slTimeSlot = 21;
        int slVongDau = slTranDau*2-1;

        //Khởi tạo
        solveGA.initRanDomQuanThe(slCaThe, slTrongTai, slSVD, slTranDau, slTimeSlot, slVongDau);
        System.out.println("**************** Bước Khởi Tạo ****************");
        for (int i = 0; i < slCaThe; i++) {
            for (int k = 0; k < slTranDau; k++)
                System.out.println(QuanThe[i][k][0] + " " + QuanThe[i][k][1] + " " + QuanThe[i][k][2]);
        }

        //Chọn lọc
//        System.out.println("**************** Bước Sắp Xếp Chọn Lọc ****************");
//        solveGA.calcuDoThichNghi(slCaThe, slTrongTai, slSVD, slTranDau, slTimeSlot, slVongDau);

        //Lai Ghép
        System.out.println("**************** Bước Lai Ghép ****************");
        int viTriLaiGhep = 3;
        int QuanTheMoi[][][][] = solveGA.methodLaiGhep(QuanThe, viTriLaiGhep, slCaThe, slTrongTai, slSVD, slTranDau, slTimeSlot, slVongDau);
        for (int i = 0; i < slCaThe; i++) {
            System.out.println("Ca the " + (i + 1) + ":");
            for (int v=0;v<slVongDau;v++)
            for (int k = 0; k < 5; k++) {
                System.out.print(QuanThe[i][v][k][0] + " " + QuanThe[i][v][k][1] + " " + QuanThe[i][v][k][2] + ":");
                System.out.println(QuanTheMoi[i][v][k][0] + " " + QuanTheMoi[i][v][k][1] + " " + QuanTheMoi[i][v][k][2]);
            }
        }
    }
}
