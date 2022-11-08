package com.example.managefootballtournament.geneticAlgorithm.test;

import com.example.managefootballtournament.geneticAlgorithm.WeightConstraint;
import com.example.managefootballtournament.geneticAlgorithm.entityGA.ScheduleGAv1;
import com.example.managefootballtournament.geneticAlgorithm.entityGA.PopulationGA;
import com.example.managefootballtournament.geneticAlgorithm.entityGA.MatchGA;
import com.example.managefootballtournament.geneticAlgorithm.entityGA.RoundGA;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Component
public class SolveGAv2 {
    Random random = new Random();

    public PopulationGA initRanDomQuanThe(int slCaThe, int amountReferee, int amountStadium, int amountMatchInRound, int amountTimeslot, int amountRound) {
        List<ScheduleGAv1> listScheduleGAv1 = new ArrayList<>();
        for (int c = 0; c < slCaThe; c++) {
            List<RoundGA> listRoundGA = new ArrayList<>();
            for (int v = 0; v < amountRound; v++) {
                List<MatchGA> listMatchGA = new ArrayList<>();
                // Mỗi lần khởi tại vòng đấu thì khởi tạo lại kho chứa Timeslot
                List<Integer> listTimeSlot = initBoxTimeSlot(amountReferee, amountStadium, amountTimeslot);
                for (int t = 0; t < amountMatchInRound; t++) {
                    int maTD = t;
                    int index = random.nextInt(listTimeSlot.size());
                    int maTimeslot = listTimeSlot.get(index);
                    listTimeSlot.remove(index);
                    if(checkRepeatTimeSlot(listMatchGA,maTD,maTimeslot,amountReferee,amountStadium)==null){
                        int maTrongTai = random.nextInt(amountReferee);
                        int maSVD = random.nextInt(amountStadium);
                        MatchGA td = new MatchGA(maTD, maTimeslot, maTrongTai, maSVD);
                        listMatchGA.add(td);
                    }else{
                        listMatchGA.add(checkRepeatTimeSlot(listMatchGA,maTD,maTimeslot,amountReferee,amountStadium));
                    }
                }
                listMatchGA.sort(new Comparator<MatchGA>() {
                    @Override
                    public int compare(MatchGA match1, MatchGA match2) {
                        if (match1.getTimeslot() == match2.getTimeslot())
                            return 0;
                        else if (match1.getTimeslot() > match2.getTimeslot())
                            return 1;
                        else
                            return -1;
                    }
                });
                RoundGA vd = new RoundGA(listMatchGA);
                listRoundGA.add(vd);
            }
            ScheduleGAv1 ltd = new ScheduleGAv1(listRoundGA,0);
            listScheduleGAv1.add(ltd);
        }
        PopulationGA quanthe = new PopulationGA(listScheduleGAv1);
        return quanthe;
    }

    public boolean constraintConfident(List<MatchGA> listMatchGA, int amountTimeslot, int amountReferee, int amountStadium) {
        int arr[] = new int[amountTimeslot + 1];
        for (int i = 1; i <= amountTimeslot; i++)
            arr[i] = 0;
        for (MatchGA td : listMatchGA) {
            arr[td.getTimeslot()] += 1;
        }
        for (int i = 1; i <= amountTimeslot; i++) {
            if (arr[i] > amountStadium || arr[i] > amountReferee)
                return false;
        }
        return true;
    }

    public int constraintReferee(List<MatchGA> listMatchGA, int amountStadiumInDay) {
        //Độ thích nghi
        int fitness = 0;
        int loop = 1;
        for (int i = 0; i < listMatchGA.size() - 1; i++) {

            // 1. Hai khoảng thời gian liên tiếp cùng 1 trọng tài
            if (listMatchGA.get(i).getTimeslot() % amountStadiumInDay != 0 &&
                    listMatchGA.get(i).getTimeslot() + 1 == listMatchGA.get(i + 1).getTimeslot() &&
                    listMatchGA.get(i).getTrongtai() == listMatchGA.get(i + 1).getTrongtai())
                // 1.1. Bắt ở 2 SVD khác nhau
                if (listMatchGA.get(i).getSanvandong() != listMatchGA.get(i + 1).getSanvandong())
                    fitness += WeightConstraint.CONSECUTIVE_REFEREE_TWO_STADIUM;
                    // 1.2. Bắt ở cùng 1 SVD
                else fitness += WeightConstraint.CONSECUTIVE_REFEREE_ONE_STADIUM;

            // 2. Trong 1 vòng đấu trọng tài phải bắt liên tiếp N trận (Case 1: =2 hoặc Case 2: >=3)
            if (listMatchGA.get(i).getTrongtai() == listMatchGA.get(i + 1).getTrongtai()) {
                loop++;
                if (i + 1 == listMatchGA.size() - 1) {
                    System.out.println("loop:" + loop);
                    if (loop == 2)
                        fitness += WeightConstraint.CONSECUTIVE_REFEREE_2;
                    if (loop >= 3)
                        fitness += WeightConstraint.CONSECUTIVE_REFEREE_3;
                }
            } else if (loop > 1) {
                System.out.println("loop:" + loop);
                if (loop == 2)
                    fitness += WeightConstraint.CONSECUTIVE_REFEREE_2;
                if (loop >= 3)
                    fitness += WeightConstraint.CONSECUTIVE_REFEREE_3;
                loop = 1;
            }
        }

        System.out.println("Do thich nghi:  " + fitness);
        return fitness;
    }

    //Ràng buộc về khoảng trống không thi đấu trong vòng đấu
    //2 ngày thi đấu k được dàn đều
    public int constraintRound(List<MatchGA> listMatchGA, int amountStadiumInDay) {
        int fitness = 0;
        for (int i = 0; i < listMatchGA.size() - 1; i++) {
            if ((listMatchGA.get(i + 1).getTimeslot() - listMatchGA.get(i).getTimeslot()) >= amountStadiumInDay * 2)
                fitness += WeightConstraint.CONSECUTIVE_SPACE_IN_ROUND;
        }
        return fitness;
    }

    public int calcufitness(List<RoundGA> listRoundGA, int amountStadiumInDay) {
        int fitness = 0;
        for (RoundGA round : listRoundGA) {
            fitness += constraintReferee(round.getListMatchGA(), amountStadiumInDay)
                    + constraintRound(round.getListMatchGA(), amountStadiumInDay);
        }
        System.out.println(fitness);
        return fitness;
    }

    public PopulationGA Laighep(List<ScheduleGAv1> scheduleGAv1List, int index) {
        PopulationGA populationGA = new PopulationGA(scheduleGAv1List);
        for (int c = 0; c < populationGA.getListScheduleGAv1().size(); c += 2) {
            List<RoundGA> listRoundGA1 = populationGA.getListScheduleGAv1().get(c).getListRoundGA();
            List<RoundGA> listRoundGA2 = populationGA.getListScheduleGAv1().get(c + 1).getListRoundGA();
            for (int v = 0; v < listRoundGA1.size(); v++) {
                List<MatchGA> listMatchGA1 = new ArrayList<>();
                listMatchGA1.addAll(listRoundGA1.get(v).getListMatchGA().subList(0, index));
                List<MatchGA> listMatchGA2 = new ArrayList<>();
                listMatchGA2.addAll(listRoundGA2.get(v).getListMatchGA().subList(0, index));
                List<MatchGA> listSubList1 = listRoundGA1.get(v).getListMatchGA().subList(index, listRoundGA1.get(v).getListMatchGA().size());
                List<MatchGA> listSubList2 = listRoundGA2.get(v).getListMatchGA().subList(index, listRoundGA1.get(v).getListMatchGA().size());
                listMatchGA1.addAll(listSubList2);
                listMatchGA2.addAll(listSubList1);

                RoundGA vd1 = new RoundGA(listMatchGA1);
                listRoundGA1.remove(v);
                listRoundGA1.add(v, vd1);
                RoundGA vd2 = new RoundGA(listMatchGA2);
                listRoundGA2.remove(v);
                listRoundGA2.add(v, vd2);
            }
        }
        return populationGA;
    }

    public List<RoundGA> methodSolveGA(int slCaThe, int amountReferee, int amountStadium, int amountMatchInRound, int amountTimeslot, int amountRound, int amountTimeslotInday) {
        //Khởi tạo
        System.out.println("**************** Bước Khởi Tạo ****************");
        PopulationGA populationGAInit = new PopulationGA(initRanDomQuanThe(slCaThe, amountReferee, amountStadium, amountMatchInRound, amountTimeslot, amountRound).getListScheduleGAv1());
        //Độ Thích Nghi
        List<ScheduleGAv1> listScheduleGAv1 = populationGAInit.getListScheduleGAv1();
        for(int i = 0; i< listScheduleGAv1.size(); i++){
            ScheduleGAv1 scheduleGAv1 = new ScheduleGAv1(listScheduleGAv1.get(i).getListRoundGA(),calcufitness(listScheduleGAv1.get(i).getListRoundGA(), amountTimeslotInday));
            listScheduleGAv1.remove(i);
            listScheduleGAv1.add(i, scheduleGAv1);
        }
        //Sort Giam Dan
        listScheduleGAv1.sort(new Comparator<ScheduleGAv1>() {
            @Override
            public int compare(ScheduleGAv1 o1, ScheduleGAv1 o2) {
                if (o1.getFitness() == o2.getFitness())
                    return 0;
                else if (o1.getFitness() < o2.getFitness())
                    return 1;
                else
                    return -1;
            }
        });
        listScheduleGAv1.forEach(ltd -> {
            System.out.println("Lich Thi Dau");
            System.out.println(ltd.getFitness());
        });
        populationGAInit.setListScheduleGAv1(listScheduleGAv1);
        //Lai Ghep
        System.out.println("**************** Bước Lai Ghép ****************");
        //QuanThe quanTheMoi = new QuanThe(Laighep(quanTheInit.getListLichThiDau(), 3).getListLichThiDau());

        return populationGAInit.getListScheduleGAv1().get(0).getListRoundGA();
    }


    public static void main(String[] args) {
        SolveGAv2 solveGAv2 = new SolveGAv2();
        int slCaThe = 10;
        int amountReferee = 2;
        int amountStadium = 2;
        int amountMatchInRound = 5;
        int amountTimeslot = 21;
        int amountTimeslotInday = 7;
        int amountRound = amountMatchInRound * 2 - 1;

        //Khởi tạo
        System.out.println("**************** Bước Khởi Tạo ****************");
        PopulationGA populationGAInit = new PopulationGA(solveGAv2.initRanDomQuanThe(slCaThe, amountReferee, amountStadium, amountMatchInRound, amountTimeslot, amountRound).getListScheduleGAv1());
        List<ScheduleGAv1> listScheduleGAv1 = populationGAInit.getListScheduleGAv1();
//        listLichThiDau.forEach(i -> solveGAv2.calcuDoThichNghi(i.getListVongDau(), amountTimeslot,amountReferee,amountStadium));
        listScheduleGAv1.forEach(ltd -> {
            System.out.println("Lich Thi Dau");
            solveGAv2.calcufitness(ltd.getListRoundGA(), amountTimeslotInday);
            ltd.getListRoundGA().forEach(vd -> {
                System.out.println("Vong Dau");
                solveGAv2.constraintReferee(vd.getListMatchGA(), amountTimeslotInday);
                vd.getListMatchGA().forEach(td -> {
                    System.out.println(td.getMaTD() + " " + td.getTimeslot() + " " + td.getTrongtai() + " " + td.getSanvandong());
                });
            });
        });


        //Lai Ghep
//        System.out.println("**************** Bước Lai Ghép ****************");
//        QuanThe quanTheLG = new QuanThe(solveGAv2.Laighep(quanTheInit.getListLichThiDau(), 3).getListLichThiDau());
//        List<LichThiDau> listLichThiDauLG = quanTheLG.getListLichThiDau();
//        listLichThiDauLG.forEach(ltd -> {
//            System.out.println("Lich Thi Dau");
//            ltd.getListVongDau().forEach(vd -> {
//                System.out.println("Vong Dau");
//                vd.getListTranDau().forEach(td -> {
//                    System.out.println(td.getMaTD() + " " + td.getTimeslot() + " " + td.getTrongtai() + " " + td.getSanvandong());
//                });
//            });
//        });
    }

    /**
    Box chứa timeslot để không random các trận đấu cùng 1 khung giờ vượt quá số lượng
    trọng tài hoặc số lượng sân vận động => tạo ra kho chứa timeslot với tổng số timeslot
    nhân với số lượng trọng tài hoặc số lượng SVD (nhân với cái nhỏ hơn)
    Vậy timeslot nào đc lấy ra sẽ xóa khỏi List(Kho chứa). => THỎA MÃN RÀNG BUỘC SỐ LƯỢNG TRẬN ĐẤU
    diễn ra trong 1 khung giờ vượt quá số lượng TT vs số lượng SVD (vô lý)
     */
    public List<Integer> initBoxTimeSlot(int amountReferee, int amountStadium, int amountTimeslot) {
        List<Integer> listInit = new ArrayList<>();
        if (amountReferee >= amountStadium)
            for (int i = 0; i < amountStadium; i++)
                for (int j = 1; j <= amountTimeslot; j++)
                    listInit.add(j);
        else {
            for (int i = 0; i < amountReferee; i++)
                for (int j = 1; j <= amountTimeslot; j++)
                    listInit.add(j);
        }
        return listInit;
    }
    public MatchGA checkRepeatTimeSlot(List<MatchGA> listMatchGA,int maTD, int maTimeSlot, int amountMatchInRound, int amountStadium) {
        for (MatchGA matchGA : listMatchGA) {
            if (matchGA.getTimeslot() == maTimeSlot) {
                List<Integer> listReferee = initListRefereeInt(amountMatchInRound, matchGA.getTrongtai());
                List<Integer> listStadium = initListStadiumInt(amountStadium, matchGA.getSanvandong());
                int maTrongTai = listReferee.get(random.nextInt(listReferee.size()));
                int maSVD = listStadium.get(random.nextInt(listStadium.size()));
                return new MatchGA(maTD,maTimeSlot,maTrongTai,maSVD);
            }
        }
        return null;
    }
    // Khởi tạo lại list trọng tài loại bỏ trọng tài đã được dùng trong 1 khung giờ
    public List<Integer> initListRefereeInt(int amountMatchInRound, int refereeDrop) {
        List<Integer> listRefereeInt = new ArrayList<>();
        for (int i = 0; i < amountMatchInRound; i++)
            if (i != refereeDrop)
                listRefereeInt.add(i);
        return listRefereeInt;
    }

    public List<Integer> initListStadiumInt(int amountStadium, int stadiumDrop) {
        List<Integer> listStadiumInt = new ArrayList<>();
        for (int i = 0; i < amountStadium; i++)
            if (i != stadiumDrop)
                listStadiumInt.add(i);
        return listStadiumInt;
    }
}


