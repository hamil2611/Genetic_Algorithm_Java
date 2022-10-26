package com.example.managefootballtournament.geneticAlgorithm;

import com.example.managefootballtournament.entityGA.LichThiDau;
import com.example.managefootballtournament.entityGA.QuanThe;
import com.example.managefootballtournament.entityGA.TranDau;
import com.example.managefootballtournament.entityGA.VongDau;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Component
public class SolveGAv2 {
    Random random = new Random();

    public QuanThe initRanDomQuanThe(int slCaThe, int slTrongTai, int slSVD, int slTranDau, int slTimeSlot, int slVongDau) {
        List<LichThiDau> listLichThiDau = new ArrayList<>();
        for (int c = 0; c < slCaThe; c++) {
            List<VongDau> listVongDau = new ArrayList<>();
            for (int v = 0; v < slVongDau; v++) {
                List<TranDau> listTranDau = new ArrayList<>();
                for (int t = 0; t < slTranDau; t++) {
                    int maTD = t;
                    int maTimeslot = random.nextInt(slTimeSlot);
                    int maTrongTai = random.nextInt(slTrongTai);
                    int maSVD = random.nextInt(slSVD);
                    TranDau td = new TranDau(maTD, maTimeslot, maTrongTai, maSVD);
                    listTranDau.add(td);
                }
                listTranDau.sort(new Comparator<TranDau>() {
                    @Override
                    public int compare(TranDau td1, TranDau td2) {
                        if (td1.getTimeslot() == td2.getTimeslot())
                            return 0;
                        else if (td1.getTimeslot() > td2.getTimeslot())
                            return 1;
                        else
                            return -1;
                    }
                });
                VongDau vd = new VongDau(listTranDau);
                listVongDau.add(vd);
            }
            LichThiDau ltd = new LichThiDau(listVongDau);
            listLichThiDau.add(ltd);
        }
        QuanThe quanthe = new QuanThe(listLichThiDau);
        return quanthe;
    }

    public QuanThe Laighep(QuanThe quanTheCu, int index) {
        QuanThe quanThe = new QuanThe(quanTheCu.getListLichThiDau());
        for (int c = 0; c < quanThe.getListLichThiDau().size(); c += 2) {
            List<VongDau> listVongDau1 = quanThe.getListLichThiDau().get(c).getListVongDau();
            List<VongDau> listVongDau2 = quanThe.getListLichThiDau().get(c + 1).getListVongDau();
            for (int v = 0; v < listVongDau1.size(); v++) {
                List<TranDau> listTranDau1 = new ArrayList<>();
                listTranDau1.addAll(listVongDau1.get(v).getListTranDau().subList(0, index));
                List<TranDau> listTranDau2 = new ArrayList<>();
                listTranDau2.addAll(listVongDau2.get(v).getListTranDau().subList(0, index));
                List<TranDau> listSubList1 = listVongDau1.get(v).getListTranDau().subList(index, listVongDau1.get(v).getListTranDau().size());
                List<TranDau> listSubList2 = listVongDau2.get(v).getListTranDau().subList(index, listVongDau1.get(v).getListTranDau().size());
                listTranDau1.addAll(listSubList2);
                listTranDau2.addAll(listSubList1);

                VongDau vd1 = new VongDau(listTranDau1);
                listVongDau1.remove(v);
                listVongDau1.add(v, vd1);
                VongDau vd2 = new VongDau(listTranDau2);
                listVongDau2.remove(v);
                listVongDau2.add(v, vd2);
            }
        }
        return quanThe;
    }

    public List<VongDau> methodSolveGA(int slCaThe, int slTrongTai, int slSVD, int slTranDau, int slTimeSlot, int slVongDau) {
        //Khởi tạo
        System.out.println("**************** Bước Khởi Tạo ****************");
        QuanThe quanTheInit = new QuanThe(initRanDomQuanThe(slCaThe, slTrongTai, slSVD, slTranDau, slTimeSlot, slVongDau).getListLichThiDau());

        //Lai Ghep
        System.out.println("**************** Bước Lai Ghép ****************");
        QuanThe quanTheMoi = new QuanThe(Laighep(quanTheInit, 3).getListLichThiDau());

        return quanTheMoi.getListLichThiDau().get(0).getListVongDau();
    }

    public static void main(String[] args) {
        SolveGAv2 solveGAv2 = new SolveGAv2();
        int slCaThe = 4;
        int slTrongTai = 2;
        int slSVD = 2;
        int slTranDau = 5;
        int slTimeSlot = 21;
        int slVongDau = slTranDau * 2 - 1;

        //Khởi tạo
        System.out.println("**************** Bước Khởi Tạo ****************");
        QuanThe quanTheInit = new QuanThe(solveGAv2.initRanDomQuanThe(slCaThe, slTrongTai, slSVD, slTranDau, slTimeSlot, slVongDau).getListLichThiDau());
        List<LichThiDau> listLichThiDau = quanTheInit.getListLichThiDau();
        listLichThiDau.forEach(ltd -> {
            System.out.println("Lich Thi Dau");
            ltd.getListVongDau().forEach(vd -> {
                System.out.println("Vong Dau");
                vd.getListTranDau().forEach(td -> {
                    System.out.println(td.getMaTD() + " " + td.getTimeslot() + " " + td.getTrongtai() + " " + td.getSanvandong());
                });
            });
        });

        //Lai Ghep
        System.out.println("**************** Bước Lai Ghép ****************");
        QuanThe quanTheLG = new QuanThe(solveGAv2.Laighep(quanTheInit, 3).getListLichThiDau());
        List<LichThiDau> listLichThiDauLG = quanTheLG.getListLichThiDau();
        listLichThiDauLG.forEach(ltd -> {
            System.out.println("Lich Thi Dau");
            ltd.getListVongDau().forEach(vd -> {
                System.out.println("Vong Dau");
                vd.getListTranDau().forEach(td -> {
                    System.out.println(td.getMaTD() + " " + td.getTimeslot() + " " + td.getTrongtai() + " " + td.getSanvandong());
                });
            });
        });
    }
}