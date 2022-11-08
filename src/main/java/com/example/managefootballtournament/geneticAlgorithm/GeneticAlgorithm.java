package com.example.managefootballtournament.geneticAlgorithm;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GeneticAlgorithm {
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

    public Population initRanDomQuanThe(int slCaThe, int amountReferee, int amountStadium, int amountMatchInRound, int amountTimeslot, int amountRound) {
        System.out.println("***** INIT *****");
        List<ScheduleGA> listScheduleGAGA = new ArrayList<>();
        for (int i = 0; i < slCaThe; i++) {
            int chromosome[] = new int[amountMatchInRound * amountRound * 3];
            for (int p = 1; p <= 3; p++)
                for (int r = 0; r < amountRound; r++) {
                    List<Integer> listTimeSlot = initBoxTimeSlot(amountReferee, amountStadium, amountTimeslot);
                    for (int m = 0; m < amountMatchInRound; m++) {
                        if (p == 1) {
                            int index = random.nextInt(listTimeSlot.size());
                            chromosome[r * amountMatchInRound + m] = listTimeSlot.get(index);
                            listTimeSlot.remove(index);
                        }
                        if (p == 2)
                            chromosome[amountMatchInRound * amountRound + r * amountMatchInRound + m] = random.nextInt(amountReferee);
                        if (p == 3)
                            chromosome[amountMatchInRound * amountRound * 2 + r * amountMatchInRound + m] = random.nextInt(amountStadium);
                    }
                }
            ScheduleGA scheduleGA = new ScheduleGA(chromosome, 0, 0);
            listScheduleGAGA.add(scheduleGA);
        }
        return new Population(listScheduleGAGA);
    }

    public int constraintReferee(int[] round, int amountMatchInRound, int amountTimeslotInDay) {
        int i = 0;
        int count = 1;
        int countARefereeInStadiums = 1;
        int fitness = 0;
        //SORT
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
//        System.out.print("Sort: ");
//        for (int j = 0; j < round.length; j++) {
//            System.out.print(round[j] + " ");
//        }
//        System.out.println();
        // Các khung giờ liên tiếp
        while (i < amountMatchInRound - 1) {
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
                fitness += 100;
        return fitness;
    }

    public void selection() {

    }

    public Population crossoverPopulation(Population population, int amountRound, int amountMatchInRound,
                                          int amountTimeslot, int amountReferee, int amountStadium,
                                          int mutationProbability) {
        Population newPopulation = new Population();
        List<ScheduleGA> scheduleGAS = population.getListScheduleGAGA();
        List<ScheduleGA> newScheduleGAS = new ArrayList<>();
        for (int i = 0; i < scheduleGAS.size(); i += 2) {
            int point1 = random.nextInt(amountMatchInRound * amountRound);
            int point2 = random.nextInt(amountMatchInRound * amountRound);
            while (point1 == point2)
                point2 = random.nextInt(amountMatchInRound * amountRound);
            if (point1 > point2) {
                int tmp = point1;
                point1 = point2;
                point2 = tmp;
            }
            int chromosomeParent1[] = scheduleGAS.get(i).getChromosome();
            int chromosomeParent2[] = scheduleGAS.get(i + 1).getChromosome();
            int newChromosome1[] = new int[amountMatchInRound * amountRound * 3];
            int newChromosome2[] = new int[amountMatchInRound * amountRound * 3];
            for (int r = 0; r < amountRound; r++) {
                for (int m = 0; m < amountMatchInRound; m++) {
                    if ((r * amountMatchInRound + m) < point1 || (r * amountMatchInRound + m) > point2) {
                        newChromosome1[r * amountMatchInRound + m] = chromosomeParent1[r * amountMatchInRound + m];
                        newChromosome1[amountMatchInRound * amountRound + r * amountMatchInRound + m] =
                                chromosomeParent1[amountMatchInRound * amountRound + r * amountMatchInRound + m];
                        newChromosome1[amountMatchInRound * amountRound * 2 + r * amountMatchInRound + m] =
                                chromosomeParent1[amountMatchInRound * amountRound * 2 + r * amountMatchInRound + m];

                        newChromosome2[r * amountMatchInRound + m] = chromosomeParent2[r * amountMatchInRound + m];
                        newChromosome2[amountMatchInRound * amountRound + r * amountMatchInRound + m] =
                                chromosomeParent2[amountMatchInRound * amountRound + r * amountMatchInRound + m];
                        newChromosome2[amountMatchInRound * amountRound * 2 + r * amountMatchInRound + m] =
                                chromosomeParent2[amountMatchInRound * amountRound * 2 + r * amountMatchInRound + m];
                    }
                    if (r * amountMatchInRound + m >= point1 && r * amountMatchInRound + m <= point2) {
                        newChromosome1[r * amountMatchInRound + m] = chromosomeParent2[r * amountMatchInRound + m];
                        newChromosome1[amountMatchInRound * amountRound + r * amountMatchInRound + m] =
                                chromosomeParent2[amountMatchInRound * amountRound + r * amountMatchInRound + m];
                        newChromosome1[amountMatchInRound * amountRound * 2 + r * amountMatchInRound + m] =
                                chromosomeParent2[amountMatchInRound * amountRound * 2 + r * amountMatchInRound + m];

                        newChromosome2[r * amountMatchInRound + m] = chromosomeParent1[r * amountMatchInRound + m];
                        newChromosome2[amountMatchInRound * amountRound + r * amountMatchInRound + m] =
                                chromosomeParent1[amountMatchInRound * amountRound + r * amountMatchInRound + m];
                        newChromosome2[amountMatchInRound * amountRound * 2 + r * amountMatchInRound + m] =
                                chromosomeParent1[amountMatchInRound * amountRound * 2 + r * amountMatchInRound + m];
                    }
                }
            }
            int mutationProbabilityIndividual1 = random.nextInt(100);
            if (mutationProbabilityIndividual1 < mutationProbability) {
//                System.out.println("***MUTATION***");
                int indexMutation = random.nextInt(amountMatchInRound * amountRound - 1) + 1;
                newChromosome1[indexMutation - 1] = random.nextInt(amountTimeslot);
                newChromosome1[amountMatchInRound * amountRound + indexMutation - 1] = random.nextInt(amountReferee);
                newChromosome1[amountMatchInRound * amountRound * 2 + indexMutation - 1] = random.nextInt(amountStadium);
            }
            int mutationProbabilityIndividual2 = random.nextInt(100);
            if (mutationProbabilityIndividual2 < mutationProbability) {
//                System.out.println("***MUTATION***");
                int indexMutation = random.nextInt(amountMatchInRound * amountRound - 1) + 1;
                newChromosome2[indexMutation - 1] = random.nextInt(amountTimeslot);
                newChromosome2[amountMatchInRound * amountRound + indexMutation - 1] = random.nextInt(amountReferee);
                newChromosome2[amountMatchInRound * amountRound * 2 + indexMutation - 1] = random.nextInt(amountStadium);
            }
            ScheduleGA scheduleGANew1 = new ScheduleGA(newChromosome1, 0, 0);
            ScheduleGA scheduleGANew2 = new ScheduleGA(newChromosome2, 0, 0);
            newScheduleGAS.add(scheduleGANew1);
            newScheduleGAS.add(scheduleGANew2);
        }
        newPopulation.setListScheduleGAGA(newScheduleGAS);
        return newPopulation;
    }

    public Population calcuFitness(Population population, int amountRound, int amountMatchInRound, int amountTimeslotInday) {
        List<ScheduleGA> scheduleGAS = new ArrayList<>();
        for (ScheduleGA i : population.getListScheduleGAGA()) {
            Map<Integer, int[]> map = mapRound(amountRound, amountMatchInRound, i);
            int fitness = 0;
            for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
                int value[] = entry.getValue();
                fitness += (constraintReferee(value, amountMatchInRound, amountTimeslotInday)
                        + constraintSpaceTime(value, amountMatchInRound, amountTimeslotInday));
            }
            ScheduleGA scheduleGA = new ScheduleGA(i.getChromosome(), fitness, 0);
            scheduleGAS.add(scheduleGA);
        }
        scheduleGAS.sort(new Comparator<ScheduleGA>() {
            @Override
            public int compare(ScheduleGA o1, ScheduleGA o2) {
                if (o1.getFitness() == o2.getFitness())
                    return 0;
                else if (o1.getFitness() > o2.getFitness())
                    return 1;
                else
                    return -1;
            }
        });
        return new Population(scheduleGAS);
    }

    public Map<Integer, int[]> mapRound(int amountRound, int amountMatchInRound, ScheduleGA scheduleGA) {
        Map<Integer, int[]> map = new HashMap<>();
        int[] chromosome = scheduleGA.getChromosome();
        for (int r = 0; r < amountRound; r++) {
            int a[] = new int[amountMatchInRound * 3];
            for (int m = 0; m < amountMatchInRound; m++) {
                a[m] = chromosome[r * amountMatchInRound + m];
                a[amountMatchInRound + m] = chromosome[amountMatchInRound * amountRound + r * amountMatchInRound + m];
                a[amountMatchInRound * 2 + m] = chromosome[amountMatchInRound * amountRound * 2 + r * amountMatchInRound + m];
            }
            map.put(r + 1, a);
        }
        return map;
    }

    public Map<Integer, int[]> solveGeneticAlgorithm(int amountRound, int amountMatchInRound) {
        ScheduleGA scheduleGA = new ScheduleGA();

        return mapRound(amountRound, amountMatchInRound, scheduleGA);
    }

    public static void main(String[] args) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        int slCaThe = 10;
        int amountReferee = 15;
        int amountStadium = 10;
        int amountMatchInRound = 5;
        int amountTimeslot = 21;
        int amountTimeslotInday = 3;
        int amountRound = amountMatchInRound * 2 - 1;
        int mutationProbability = 10;
        Population population = geneticAlgorithm.initRanDomQuanThe(slCaThe, amountReferee, amountStadium, amountMatchInRound, amountTimeslot, amountRound);

//        for (ScheduleGAv4 i : population.getListScheduleGA()) {
//            Map<Integer, int[]> map = solveGAv4.mapRound(amountRound, amountMatchInRound, i);
//            int fitness = 0;
//            for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
//                System.out.println("---- " + entry.getKey() + " ----");
//                int value[] = entry.getValue();
//                for (int j = 0; j < value.length; j++)
//                    System.out.print(value[j] + " ");
//
//                fitness += solveGAv4.constraintReferee(value, amountMatchInRound, amountTimeslotInday);
//                System.out.println("----------");
//            }
//            System.out.println("fitness: " + fitness);
//            ScheduleGAv4 scheduleGAv4 = new ScheduleGAv4(i.getChromosome(), fitness);
//            scheduleGAv4s.add(scheduleGAv4);
//        }
//        population.setListScheduleGA(scheduleGAv4s);
        population.setListScheduleGAGA(geneticAlgorithm.calcuFitness(population, amountRound, amountMatchInRound, amountTimeslotInday).getListScheduleGAGA());
        System.out.println("*******************FITNESS******************");
        for (ScheduleGA i : population.getListScheduleGAGA()) {
            Map<Integer, int[]> map = geneticAlgorithm.mapRound(amountRound, amountMatchInRound, i);
            for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
                System.out.println("---- " + entry.getKey() + " ----");
                int value[] = entry.getValue();
                for (int j = 0; j < value.length; j++)
                    System.out.print(value[j] + " ");
                System.out.println("----------");
            }
            System.out.println("FITNESS: " + i.getFitness());
        }

        System.out.println("*******************CROSSOVER******************");
        List<ScheduleGA> scheduleGAS = new ArrayList<>();
        int fitnessBest = population.getListScheduleGAGA().get(0).getFitness();
        while (fitnessBest > 100) {
            Population crossoverPopulation = geneticAlgorithm.crossoverPopulation(population, amountRound, amountMatchInRound, amountTimeslot, amountReferee, amountStadium, mutationProbability);
            crossoverPopulation.setListScheduleGAGA(geneticAlgorithm.calcuFitness(crossoverPopulation, amountRound, amountMatchInRound, amountTimeslotInday).getListScheduleGAGA());
            fitnessBest = crossoverPopulation.getListScheduleGAGA().get(0).getFitness();

        }
        System.out.println("fitnessBest: " + fitnessBest);
    }
}


