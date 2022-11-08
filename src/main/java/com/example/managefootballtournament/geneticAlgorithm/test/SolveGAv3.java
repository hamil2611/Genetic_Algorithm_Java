package com.example.managefootballtournament.geneticAlgorithm.test;

import com.example.managefootballtournament.geneticAlgorithm.WeightConstraint;
import com.example.managefootballtournament.geneticAlgorithm.entityGA.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Component
public class SolveGAv3 {
    Random random = new Random();

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

    public List<Integer> initListRefereeInt(List<Integer> listRefereeInt, int amountReferee) {
        List<Integer> listRefereeIntNotExist = new ArrayList<>();
        for (int i = 0; i < amountReferee; i++)
            if (!listRefereeInt.contains(i))
                listRefereeIntNotExist.add(i);
        return listRefereeIntNotExist;
    }

    public List<Integer> initListStadiumInt(List<Integer> listStaidumInt, int amountStadium) {
        List<Integer> listStadiumIntNotExist = new ArrayList<>();
        for (int i = 0; i < amountStadium; i++)
            if (!listStaidumInt.contains(i))
                listStadiumIntNotExist.add(i);
        return listStadiumIntNotExist;
    }

    public int[] validateRoundArr(int[] round, int amountMatchInRound, int amountReferee, int amountStadium) {
        int i = 0;
        List<Integer> listRefereeInt = new ArrayList<>();
        List<Integer> listStadiumInt = new ArrayList<>();
        while (i < amountMatchInRound) {
            if (round[i] == round[i + 1]) {
                listRefereeInt.add(round[amountMatchInRound + i]);
                listStadiumInt.add(round[amountMatchInRound * 2 + i]);
                i++;
            } else {
                listRefereeInt.add(round[amountMatchInRound + i]);
                listStadiumInt.add(round[amountMatchInRound * 2 + i]);
                i++;
//                System.out.println("size list" + listRefereeInt.size());
                if (listRefereeInt.size() >= 2) {
                    for (int j = i - listRefereeInt.size(); j < i - 1; j++) {
//                        System.out.println("size"+ j);
                        //VALIDATE TRONG TAI
                        List<Integer> listRefereeIntExist = new ArrayList<>();
                        List<Integer> listRefereeIntNotExist = initListRefereeInt(listRefereeInt, amountReferee);
                        if (round[j + amountMatchInRound] == round[j + amountMatchInRound + 1]) {
                            if (!listRefereeIntNotExist.isEmpty()) {
                                int index = random.nextInt(listRefereeIntNotExist.size());
                                round[j + amountMatchInRound] = listRefereeIntNotExist.get(index);
                                listRefereeIntNotExist.remove(index);
                            }
                        } else {
                            if (listRefereeIntExist.contains(round[j + amountMatchInRound])) {
                                int index = random.nextInt(listRefereeIntNotExist.size());
                                round[j + amountMatchInRound] = listRefereeIntNotExist.get(index);
                                listRefereeIntNotExist.remove(index);
                            } else {
                                listRefereeIntExist.add(round[j + amountMatchInRound]);
                            }
                        }
                        //VALIDATE SVD
                        List<Integer> listStadiumIntExist = new ArrayList<>();
                        List<Integer> listStadiumIntNotExist = initListStadiumInt(listStadiumInt, amountStadium);
                        if (round[j + amountMatchInRound * 2] == round[j + amountMatchInRound * 2 + 1]) {
                            if (!listStadiumIntNotExist.isEmpty()) {
                                int index = random.nextInt(listStadiumIntNotExist.size());
                                round[j + amountMatchInRound * 2] = listStadiumIntNotExist.get(index);
                                listStadiumIntNotExist.remove(index);
                            }
                        } else {
                            if (listStadiumIntExist.contains(round[j + amountMatchInRound * 2])) {
                                int index = random.nextInt(listStadiumIntNotExist.size());
                                round[j + amountMatchInRound * 2] = listStadiumIntNotExist.get(index);
                                listStadiumIntNotExist.remove(index);
                            } else {
                                listStadiumIntExist.add(round[j + amountMatchInRound * 2]);
                            }
                        }
                        listRefereeInt.clear();
                        listStadiumInt.clear();
                    }
                } else {
                    listRefereeInt.clear();
                    listStadiumInt.clear();
                }

            }
        }
        return round;
    }

    public List<ScheduleGAv3> initRanDomQuanThe(int slCaThe, int amountReferee, int amountStadium, int amountMatchInRound, int amountTimeslot, int amountRound) {
        List<ScheduleGAv3> listScheduleGA = new ArrayList<>();
        for (int c = 0; c < slCaThe; c++) {
            List<int[]> listRound = new ArrayList<>();
            for (int v = 0; v < amountRound; v++) {
                int round[] = new int[amountMatchInRound * 3];
                List<Integer> listTimeSlot = initBoxTimeSlot(amountReferee, amountStadium, amountTimeslot);
                for (int p = 1; p <= 3; p++)
                    for (int t = amountMatchInRound * (p - 1); t < amountMatchInRound * p; t++) {
                        if (p == 1) {
                            int index = random.nextInt(listTimeSlot.size());
                            round[t] = listTimeSlot.get(index);
                            listTimeSlot.remove(index);
                        }
                        if (p == 2)
                            round[t] = random.nextInt(amountReferee);
                        if (p == 3)
                            round[t] = random.nextInt(amountStadium);
                    }
                //sort tang dan
                for (int p = 1; p <= 3; p++)
                    for (int t = 0; t < amountMatchInRound; t++) {
                        for (int k = 0; k < amountMatchInRound - 1; k++)
                            if (round[k] > round[k + 1]) {
                                int tmp = round[k];
                                round[k] = round[k + 1];
                                round[k + 1] = tmp;
                                for (int l = 1; l <= 2; l++) {
                                    int tmp1 = round[k + amountMatchInRound * l];
                                    round[k + amountMatchInRound * l] = round[k + amountMatchInRound * l + 1];
                                    round[k + amountMatchInRound * l + 1] = tmp1;
                                }
                            }
                    }
                //
//                for(int u=0;u<round.length;u++)
//                    System.out.print(round[u]+ " " );
//                System.out.println();
//                System.out.println("----------------------");
                round = validateRoundArr(round, amountMatchInRound, amountReferee, amountStadium);
                listRound.add(round);
            }
            ScheduleGAv3 scheduleGAv3 = new ScheduleGAv3(listRound, 0);
            listScheduleGA.add(scheduleGAv3);
        }
        return listScheduleGA;
    }

    public int constraintReferee(int[] round, int amountMatchInRound, int amountTimeslotInDay) {
        int i = 0;
        int count = 1;
        int countARefereeInStadiums = 1;
        int fitness = 0;
        // Các khung giờ liên tiếp
        while (i < amountMatchInRound) {
            if (round[i] == (round[i + 1] + 1) && round[i] % amountTimeslotInDay != 0 && round[i + amountMatchInRound] == round[i + amountMatchInRound + 1]) {
                count++;
                if (round[i + amountMatchInRound * 2] == round[i + amountMatchInRound * 2 + 1])
                    countARefereeInStadiums++;
                i++;
            } else {
                if (countARefereeInStadiums == 1) {
                    if (count == 2)
                        fitness += WeightConstraint.CONSECUTIVE_REFEREE_ONE_STADIUM;
                    else if (count > 2)
                        fitness += WeightConstraint.CONSECUTIVE_REFEREE_ONE_STADIUM + WeightConstraint.CONSECUTIVE_REFEREE_3;
                } else {
                    fitness += WeightConstraint.CONSECUTIVE_REFEREE_TWO_STADIUM;
                }
                i++;
            }
        }
        for (int j = amountMatchInRound * 2; j < amountMatchInRound * 3; j++) {

        }
        int j = amountMatchInRound * 2;
        int countReferee = 1;
        while (j < amountMatchInRound * 3 - 1) {
            if (round[j] == round[j + 1]) {
                countReferee++;
                j++;
            } else {
                if (countReferee == 2)
                    fitness += WeightConstraint.CONSECUTIVE_REFEREE_2;
                if (countReferee > 2)
                    fitness += WeightConstraint.CONSECUTIVE_REFEREE_3;
                countReferee = 1;
                j++;
            }
        }
        return fitness;
    }

    public int constraintSpaceTime(int[] round, int amountMatchInRound, int amountTimeslotInDay) {
        int fitness = 0;
        for (int i = 0; i < amountMatchInRound - 1; i++)
            if (round[i + 1] - round[i] >= 2 * amountTimeslotInDay)
                fitness += WeightConstraint.CONSECUTIVE_SPACE_IN_ROUND;
        return fitness;
    }

    public int calcuFitness(List<int[]> listRound, int amountMatchInRound, int amountTimeslotInDay) {
        int fitness = 0;
        for (int i = 0; i < listRound.size(); i++)
            fitness += (constraintReferee(listRound.get(i), amountMatchInRound, amountTimeslotInDay) +
                    constraintSpaceTime(listRound.get(i), amountMatchInRound, amountTimeslotInDay));
        return fitness;
    }

    public void selectParent(){

    }

    public void crossoverPopulation(){

    }
    public static void main(String[] args) {
        SolveGAv3 solveGAv3 = new SolveGAv3();
        int slCaThe = 1000;
        int amountReferee = 3;
        int amountStadium = 2;
        int amountMatchInRound = 5;
        int amountTimeslot = 21;
        int amountTimeslotInday = 3;
        int amountRound = amountMatchInRound * 2 - 1;
        List<ScheduleGAv3> scheduleGAv3s = solveGAv3.initRanDomQuanThe(slCaThe, amountReferee, amountStadium, amountMatchInRound, amountTimeslot, amountRound);
        for (int i = 0; i < scheduleGAv3s.size(); i++) {
            int fitness = 0;
            fitness = solveGAv3.calcuFitness(scheduleGAv3s.get(i).getListRound(), amountMatchInRound, amountTimeslotInday);
            ScheduleGAv3 scheduleGAv3 = new ScheduleGAv3(scheduleGAv3s.get(i).getListRound(), fitness);
            scheduleGAv3s.remove(i);
            scheduleGAv3s.add(i, scheduleGAv3);
        }
        scheduleGAv3s.sort(new Comparator<ScheduleGAv3>() {
            @Override
            public int compare(ScheduleGAv3 o1, ScheduleGAv3 o2) {
                if (o1.getFitness() == o2.getFitness())
                    return 0;
                else if (o1.getFitness() < o2.getFitness())
                    return 1;
                else
                    return -1;
            }
        });
        System.out.println("********");
        scheduleGAv3s.forEach(i -> {
            System.out.println("----------");
            System.out.println("fitness: " + i.getFitness());
            i.getListRound().forEach(r -> {
                for (int j = 0; j < r.length; j++)
                    System.out.print(r[j] + " ");
                System.out.println();
            });
        });
    }

}


