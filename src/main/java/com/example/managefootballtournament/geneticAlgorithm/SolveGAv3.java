package com.example.managefootballtournament.geneticAlgorithm;

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

    public int[] validateRoundArr(int[] round, int amountMatch, int amountReferee, int amountStadium) {
        int i = 0;
        List<Integer> listRefereeInt = new ArrayList<>();
        List<Integer> listStadiumInt = new ArrayList<>();
        while (i < amountMatch) {
            if (round[i] == round[i + 1]) {
                listRefereeInt.add(round[amountMatch + i]);
                listStadiumInt.add(round[amountMatch * 2 + i]);
                i++;
            } else {
                listRefereeInt.add(round[amountMatch + i]);
                listStadiumInt.add(round[amountMatch * 2 + i]);
                i++;
//                System.out.println("size list" + listRefereeInt.size());
                if (listRefereeInt.size() >= 2) {
                    for (int j = i - listRefereeInt.size(); j < i - 1; j++) {
//                        System.out.println("size"+ j);
                        //VALIDATE TRONG TAI
                        List<Integer> listRefereeIntExist = new ArrayList<>();
                        List<Integer> listRefereeIntNotExist = initListRefereeInt(listRefereeInt, amountReferee);
                        if (round[j + amountMatch] == round[j + amountMatch + 1]) {
                            if (!listRefereeIntNotExist.isEmpty()) {
                                int index = random.nextInt(listRefereeIntNotExist.size());
                                round[j + amountMatch] = listRefereeIntNotExist.get(index);
                                listRefereeIntNotExist.remove(index);
                            }
                        } else {
                            if (listRefereeIntExist.contains(round[j + amountMatch])) {
                                int index = random.nextInt(listRefereeIntNotExist.size());
                                round[j + amountMatch] = listRefereeIntNotExist.get(index);
                                listRefereeIntNotExist.remove(index);
                            } else {
                                listRefereeIntExist.add(round[j + amountMatch]);
                            }
                        }
                        //VALIDATE SVD
                        List<Integer> listStadiumIntExist = new ArrayList<>();
                        List<Integer> listStadiumIntNotExist = initListStadiumInt(listStadiumInt, amountStadium);
                        if (round[j + amountMatch * 2] == round[j + amountMatch * 2 + 1]) {
                            if (!listStadiumIntNotExist.isEmpty()) {
                                int index = random.nextInt(listStadiumIntNotExist.size());
                                round[j + amountMatch * 2] = listStadiumIntNotExist.get(index);
                                listStadiumIntNotExist.remove(index);
                            }
                        } else {
                            if (listStadiumIntExist.contains(round[j + amountMatch * 2])) {
                                int index = random.nextInt(listStadiumIntNotExist.size());
                                round[j + amountMatch * 2] = listStadiumIntNotExist.get(index);
                                listStadiumIntNotExist.remove(index);
                            } else {
                                listStadiumIntExist.add(round[j + amountMatch * 2]);
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

    public List<ScheduleGAv3> initRanDomQuanThe(int slCaThe, int amountReferee, int amountStadium, int amountMatch, int amountTimeslot, int amountRound) {
        List<ScheduleGAv3> listScheduleGA = new ArrayList<>();
        for (int c = 0; c < slCaThe; c++) {
            List<int[]> listRound = new ArrayList<>();
            for (int v = 0; v < amountRound; v++) {
                int round[] = new int[amountMatch * 3];
                List<Integer> listTimeSlot = initBoxTimeSlot(amountReferee, amountStadium, amountTimeslot);
                for (int p = 1; p <= 3; p++)
                    for (int t = amountMatch * (p - 1); t < amountMatch * p; t++) {
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
                    for (int t = 0; t < amountMatch; t++) {
                        for (int k = 0; k < amountMatch - 1; k++)
                            if (round[k] > round[k + 1]) {
                                int tmp = round[k];
                                round[k] = round[k + 1];
                                round[k + 1] = tmp;
                                for (int l = 1; l <= 2; l++) {
                                    int tmp1 = round[k + amountMatch * l];
                                    round[k + amountMatch * l] = round[k + amountMatch * l + 1];
                                    round[k + amountMatch * l + 1] = tmp1;
                                }
                            }
                    }
                //
//                for(int u=0;u<round.length;u++)
//                    System.out.print(round[u]+ " " );
//                System.out.println();
//                System.out.println("----------------------");
                round = validateRoundArr(round, amountMatch, amountReferee, amountStadium);
                listRound.add(round);
            }
            ScheduleGAv3 scheduleGAv3 = new ScheduleGAv3(listRound, 0);
            listScheduleGA.add(scheduleGAv3);
        }
        return listScheduleGA;
    }

    public int constraintReferee(int[] round, int amountMatch, int amountTimeslotInDay) {
        int i = 0;
        int count = 1;
        int countARefereeInStadiums = 1;
        int fitness = 0;
        // Các khung giờ liên tiếp
        while (i < amountMatch) {
            if (round[i] == (round[i + 1] + 1) && round[i] % amountTimeslotInDay != 0 && round[i + amountMatch] == round[i + amountMatch + 1]) {
                count++;
                if (round[i + amountMatch * 2] == round[i + amountMatch * 2 + 1])
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
        for (int j = amountMatch * 2; j < amountMatch * 3; j++) {

        }
        int j = amountMatch * 2;
        int countReferee = 1;
        while (j < amountMatch * 3 - 1) {
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

    public int constraintSpaceTime(int[] round, int amountMatch, int amountTimeslotInDay) {
        int fitness = 0;
        for (int i = 0; i < amountMatch - 1; i++)
            if (round[i + 1] - round[i] >= 2 * amountTimeslotInDay)
                fitness += WeightConstraint.CONSECUTIVE_SPACE_IN_ROUND;
        return fitness;
    }

    public int calcuFitness(List<int[]> listRound, int amountMatch, int amountTimeslotInDay) {
        int fitness = 0;
        for (int i = 0; i < listRound.size(); i++)
            fitness += (constraintReferee(listRound.get(i), amountMatch, amountTimeslotInDay) +
                    constraintSpaceTime(listRound.get(i), amountMatch, amountTimeslotInDay));
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
        int amountMatch = 5;
        int amountTimeslot = 21;
        int amountTimeslotInday = 3;
        int amountRound = amountMatch * 2 - 1;
        List<ScheduleGAv3> scheduleGAv3s = solveGAv3.initRanDomQuanThe(slCaThe, amountReferee, amountStadium, amountMatch, amountTimeslot, amountRound);
        for (int i = 0; i < scheduleGAv3s.size(); i++) {
            int fitness = 0;
            fitness = solveGAv3.calcuFitness(scheduleGAv3s.get(i).getListRound(), amountMatch, amountTimeslotInday);
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


