package com.example.managefootballtournament.geneticAlgorithm;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GeneticAlgorithm {
    Random random = new Random();

    public List<Integer> initBoxTimeSlot(int amountReferee, int amountTimeslot) {
        List<Integer> listInit = new ArrayList<>();
        for (int i = 0; i < amountReferee; i++)
            for (int j = 0; j < amountTimeslot; j++)
                listInit.add(j);
        return listInit;
    }

    public Population initPopulation(int slCaThe, int amountReferee, int amountMatchInRound, int amountTimeslot, int amountRound) {
        System.out.println("***** INIT *****");
        List<ScheduleGA> listScheduleGAGA = new ArrayList<>();
        for (int i = 0; i < slCaThe; i++) {
            int chromosome[] = new int[amountMatchInRound * amountRound * 2];
            for (int r = 0; r < amountRound; r++) {
                List<Integer> listTimeSlot = initBoxTimeSlot(amountReferee, amountTimeslot);
                for (int m = 0; m < amountMatchInRound; m++) {
                    int index = random.nextInt(listTimeSlot.size());
                    chromosome[r * amountMatchInRound + m] = listTimeSlot.get(index);
                    listTimeSlot.remove(index);
                    chromosome[amountMatchInRound * amountRound + r * amountMatchInRound + m] = random.nextInt(amountReferee);
                }
            }
            ScheduleGA scheduleGA = new ScheduleGA(chromosome, 0, 0, 0, 0, 0,"",0);
            listScheduleGAGA.add(scheduleGA);
        }
        return new Population(listScheduleGAGA);
    }

    public FitnessGA constraintClashReferee(int[] round, int amountMatchInRound) {
        int totalConstraint = 0;
        int fitness = 0;
        int i = 0;
        List<Integer> listRefereeInt = new ArrayList<>();
        //SORT
        sortRound(round,amountMatchInRound);
        int count=0;
        while (i < amountMatchInRound-1) {
            if (round[i] == round[i + 1]) {
                count++;
                i++;
                if(i==amountMatchInRound-1){
                    i++;
                    for (int j = i - count; j < i - 1; j++) {
                        //VALIDATE TRONG TAI
                        if (round[j + amountMatchInRound] == round[j + amountMatchInRound + 1]) {
                            fitness += 100;
                            totalConstraint++;
                        }
                        count=0;
                    }

                }
            } else {
                listRefereeInt.add(round[amountMatchInRound + i]);
                i++;
                if (count > 1)
                    for (int j = i - listRefereeInt.size(); j < i - 1; j++) {
                        //VALIDATE TRONG TAI
                        if (round[j + amountMatchInRound] == round[j + amountMatchInRound + 1]) {
                            fitness += 100;
                            totalConstraint++;
                        }
                        count =0;
                    }
            }

        }
        return new FitnessGA(fitness, totalConstraint);
    }

    public FitnessGA constraintRefereeInDay(int[] round, int amountMatchInRound, int amountTimeslotInDay) {
        int i = 0;
        int fitness = 0;
        int totalConstraint = 0;
        //SORT
        sortRound(round,amountMatchInRound);
        int day = round[i] / amountTimeslotInDay;
        int count=1;
        while (i < amountMatchInRound - 1) {
            if (round[i + 1] / amountTimeslotInDay == day) {
                count ++;
                i++;
                if (i == (amountMatchInRound - 1)) {
                    i++;
                    if (count > 1) {
                        for (int j = i - count; j < i - 1; j++) {
                            // Ràng buộc trọng tài bắt 1 trận 1 ngày
                            if (round[amountMatchInRound + j] == round[amountMatchInRound + j + 1]) {
                                fitness += 10;
                                totalConstraint++;
                            }
                        }
                    }
                }
            } else {
                i++;
                if (count > 1) {
                    for (int j = i - count; j < i - 1; j++)
                        // Ràng buộc trọng tài bắt 1 trận 1 ngày
                        if (round[amountMatchInRound + j] == round[amountMatchInRound + j + 1]) {
                            fitness += 10;
                            totalConstraint++;
                        }
                    count=1;
                    day = round[i] / amountTimeslotInDay;
                } else {
                    count =1;
                    day = round[i] / amountTimeslotInDay;
                }
            }
        }
        return new FitnessGA(fitness, totalConstraint);
    }

    public FitnessGA constraintSpaceTimeInRound(int[] round, int amountMatchInRound, int amountTimeslotInDay) {
        int fitness = 0;
        int totalConstraint = 0;
        //SORT
        sortRound(round,amountMatchInRound);
        // Khoảng thời gian trống trong 1 tuần
        for (int i = 0; i < amountMatchInRound - 1; i++)
            if (round[i + 1] - round[i] >= 2 * amountTimeslotInDay - 2) {
                fitness += 1;
                totalConstraint++;
            }
        return new FitnessGA(fitness, totalConstraint);
    }

    public FitnessGA constraintSpaceTimeInDay(int[] round, int amountMatchInRound, int amountTimeslotInDay) {
        int i = 0;
        int fitness = 0;
        int totalConstraint = 0;
        //SORT
        sortRound(round,amountMatchInRound);
        int day = round[i] / amountTimeslotInDay;
        int count =1;
        while (i < amountMatchInRound - 1) {
            if (round[i + 1] / amountTimeslotInDay == day) {
                count++;
                i++;
                if (i == (amountMatchInRound - 1)) {
                    i++;
                    if (count > 1) {
                        for (int j = i - count; j < i - 1; j++)
                            // Ràng buộc khoảng thời gian trống trong 1 ngày
                            if (round[j] != round[j + 1] && round[j + 1] != round[j] + 1) {
                                fitness++;
                                totalConstraint++;
                            }
                    }
                }
            } else {
                i++;
                if (count > 1) {
                    for (int j = i - count; j < i - 1; j++)
                        // Ràng buộc khoảng thời gian trống trong 1 ngày
                        if (round[j] != round[j + 1] && round[j + 1] != round[j] + 1) {
                            fitness++;
                            totalConstraint++;
                        }
                    count=1;
                    day = round[i] / amountTimeslotInDay;
                } else {
                    count=1;
                    day = round[i] / amountTimeslotInDay;
                }
            }

        }
        return new FitnessGA(fitness, totalConstraint);
    }

    public Population crossoverPopulation(Population population, int amountRound, int amountMatchInRound,
                                          int amountTimeslot, int amountReferee, int mutationProbability, int crossoverProbability) {

        Population newPopulation = new Population();
        List<ScheduleGA> scheduleGAS = population.getListScheduleGAGA();
        List<ScheduleGA> newScheduleGAS = new ArrayList<>();
        int mask[] = new int[amountRound * amountMatchInRound * 3];
        int N = amountRound * amountMatchInRound;
        for (int i = 0; i < amountRound * amountMatchInRound * 3; i++) {
            mask[i] = random.nextInt(2);
        }
//        System.out.println("******** MASK ********");
//        System.out.print("MASK: ");
//        for (int i = 0; i < amountRound * amountMatchInRound * 3; i++) {
//            System.out.print(mask[i] + " ");
//        }
//        System.out.println();
        for (int i = 0; i < scheduleGAS.size(); i += 2) {
            int chromosomeParent1[] = scheduleGAS.get(i).getChromosome();
            int chromosomeParent2[] = scheduleGAS.get(i + 1).getChromosome();
            int newChromosome1[] = new int[amountMatchInRound * amountRound * 2];
            int newChromosome2[] = new int[amountMatchInRound * amountRound * 2];
            int crossoverProbabilityIndividual = random.nextInt(100);
            if(crossoverProbabilityIndividual < crossoverProbability){
                //
//            System.out.print("PARENT 1: ");
//            for (int l = 0; l < amountMatchInRound * amountRound * 2; l++) {
//                System.out.print(chromosomeParent1[l] + " ");
//            }
//            System.out.println();
//            System.out.print("PARENT 2: ");
//            for (int l = 0; l < amountMatchInRound * amountRound * 2; l++) {
//                System.out.print(chromosomeParent2[l] + " ");
//            }
//            System.out.println();
                for (int p = 0; p < 2; p++) {
                    for (int r = 0; r < amountRound; r++) {
                        for (int m = 0; m < amountMatchInRound; m++) {
                            if (mask[N * p + r * amountMatchInRound + m] == 1) {
                                newChromosome1[N * p + r * amountMatchInRound + m] =
                                        chromosomeParent1[N * p + r * amountMatchInRound + m];
                                newChromosome2[amountMatchInRound * amountRound * p + r * amountMatchInRound + m] =
                                        chromosomeParent2[N * p + r * amountMatchInRound + m];
                            } else {
                                newChromosome1[N * p + r * amountMatchInRound + m] =
                                        chromosomeParent2[N * p + r * amountMatchInRound + m];
                                newChromosome2[amountMatchInRound * amountRound * p + r * amountMatchInRound + m] =
                                        chromosomeParent1[N * p + r * amountMatchInRound + m];
                            }
                        }
                    }
                }
//            System.out.print("CON 1: ");
//            for (int l = 0; l < amountMatchInRound * amountRound * 2; l++) {
//                System.out.print(newChromosome1[l] + " ");
//            }
//            System.out.println();
                int mutationProbabilityIndividual1 = random.nextInt(100);
                if (mutationProbabilityIndividual1 < mutationProbability) {

                    int indexMutation = random.nextInt(N ) ;
                    int indexGenSelected = random.nextInt(N) ;
//                System.out.println("***MUTATION***");
//                System.out.println("INDEX MUTATION: " + indexMutation);
//                System.out.println("INDEX GEN SELECTED TO MUTATION: " + indexGenSelected);
                    while (indexMutation == indexGenSelected)
                        indexGenSelected = random.nextInt(N) ;
                    newChromosome1[indexMutation] = newChromosome1[indexGenSelected ];
                    newChromosome1[N + indexMutation] = newChromosome1[N+indexGenSelected ];
//                System.out.print("CON 1 SAU KHI DOT BIEN: ");
//                for (int l = 0; l < amountMatchInRound * amountRound * 2; l++) {
//                    System.out.print(newChromosome1[l] + " ");
//                }
//                System.out.println();

                }
//            System.out.print("CON 2: ");
//            for (int l = 0; l < amountMatchInRound * amountRound * 2; l++) {
//                System.out.print(newChromosome2[l] + " ");
//            }
//            System.out.println();
                int mutationProbabilityIndividual2 = random.nextInt(100);
                if (mutationProbabilityIndividual2 < mutationProbability) {

                    int indexMutation = random.nextInt(N - 1) + 1;
                    int indexGenSelected = random.nextInt(N - 1) + 1;
//                System.out.println("***MUTATION***");
//                System.out.println("INDEX MUTATION: " + indexMutation);
//                System.out.println("INDEX GEN SELECTED TO MUTATION: " + indexGenSelected);
                    while (indexMutation == indexGenSelected)
                        indexGenSelected = random.nextInt(N - 1) + 1;
                    newChromosome2[indexMutation - 1] = newChromosome2[indexGenSelected - 1];
                    newChromosome2[N + indexMutation - 1] = newChromosome2[N+indexGenSelected - 1];
//                System.out.print("CON 2 SAU KHI DOT BIEN : ");
//                for (int l = 0; l < amountMatchInRound * amountRound * 2; l++) {
//                    System.out.print(newChromosome2[l] + " ");
//                }
//                System.out.println();
                }
//            System.out.println();
            }
            else{
                newChromosome1 = chromosomeParent1;
                newChromosome2 = chromosomeParent2;
            }
            ScheduleGA scheduleGANew1 = new ScheduleGA(newChromosome1, 0, 0, 0, 0, 0,"",0);
            ScheduleGA scheduleGANew2 = new ScheduleGA(newChromosome2, 0, 0, 0, 0, 0,"",0);
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
            int totalConstraintClashRefereee = 0;
            int totalConstraintRefereeInDay = 0;
            int totalConstraintSpaceTimeInRound = 0;
            int totalConstraintSpaceTimeInDay = 0;
            for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
                int value[] = entry.getValue();
                fitness += ( constraintClashReferee(value, amountMatchInRound).getFitness()
                        + constraintRefereeInDay(value, amountMatchInRound, amountTimeslotInday).getFitness()
                        + constraintSpaceTimeInRound(value, amountMatchInRound, amountTimeslotInday).getFitness()
                        + constraintSpaceTimeInDay(value, amountMatchInRound, amountTimeslotInday).getFitness());
                totalConstraintClashRefereee += constraintClashReferee(value, amountMatchInRound).getTotalConstraint();
                totalConstraintRefereeInDay += constraintRefereeInDay(value, amountMatchInRound, amountTimeslotInday).getTotalConstraint();
                totalConstraintSpaceTimeInRound += constraintSpaceTimeInRound(value, amountMatchInRound, amountTimeslotInday).getTotalConstraint();
                totalConstraintRefereeInDay += constraintSpaceTimeInDay(value, amountMatchInRound, amountTimeslotInday).getFitness();
            }
            ScheduleGA scheduleGA = new ScheduleGA(i.getChromosome(), (float) 1 / (1 + fitness), totalConstraintClashRefereee, totalConstraintRefereeInDay, totalConstraintSpaceTimeInRound,totalConstraintSpaceTimeInDay,"",0);
            scheduleGAS.add(scheduleGA);
       }
        //Sap xep theo do thich nghi giam dan
        scheduleGAS.sort(new Comparator<ScheduleGA>() {
            @Override
            public int compare(ScheduleGA o1, ScheduleGA o2) {
                if (o1.getFitness() == o2.getFitness())
                    return 0;
                else if (o1.getFitness() < o2.getFitness())
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
            int a[] = new int[amountMatchInRound * 2];
            for (int m = 0; m < amountMatchInRound; m++) {
                a[m] = chromosome[r * amountMatchInRound + m];
                a[amountMatchInRound + m] = chromosome[amountMatchInRound * amountRound + r * amountMatchInRound + m];
            }
            map.put(r + 1, a);
        }
        return map;
    }
    public void sortRound(int[] round , int amountMatchInRound){
        for (int p = 1; p <= 2; p++)
            for (int t = 0; t < amountMatchInRound; t++) {
                for (int k = 0; k < amountMatchInRound - 1; k++)
                    if (round[k] > round[k + 1]) {

                        int tmp = round[k];
                        round[k] = round[k + 1];
                        round[k + 1] = tmp;

                        int tmp1 = round[k + amountMatchInRound];
                        round[k + amountMatchInRound] = round[k + amountMatchInRound + 1];
                        round[k + amountMatchInRound + 1] = tmp1;

                    }
            }
    }
    public static void main(String[] args) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        int slCaThe = 50;
        int amountReferee = 5;
        int amountMatchInRound = 5;
        int amountTimeslot = 9;
        int amountTimeslotInday = 3;
        int amountRound = amountMatchInRound * 2 - 1;
        int mutationProbability = 5;
        int crossoverProbability =80;
        Population population = geneticAlgorithm.initPopulation(slCaThe, amountReferee, amountMatchInRound, amountTimeslot, amountRound);

        population.setListScheduleGAGA(geneticAlgorithm.calcuFitness(population, amountRound, amountMatchInRound, amountTimeslotInday).getListScheduleGAGA());
        System.out.println("*******************FITNESS******************");

        System.out.println("*******************CROSSOVER******************");
        List<ScheduleGA> scheduleGAS = new ArrayList<>();
        float fitnessBest = population.getListScheduleGAGA().get(0).getFitness();
        int count = 0;
        Population crossoverPopulation = population;
        while (fitnessBest < 1.0) {
            crossoverPopulation = geneticAlgorithm.crossoverPopulation(population, amountRound, amountMatchInRound, amountTimeslot, amountReferee, mutationProbability, crossoverProbability);
            crossoverPopulation = geneticAlgorithm.calcuFitness(crossoverPopulation, amountRound, amountMatchInRound, amountTimeslotInday);
            fitnessBest = crossoverPopulation.getListScheduleGAGA().get(0).getFitness();
            count++;
            if (count % 100 == 0) {
                System.out.println("fitnessBest: " + count + " : " + fitnessBest);
            }
        }
        for (ScheduleGA i : crossoverPopulation.getListScheduleGAGA()) {
            Map<Integer, int[]> map = geneticAlgorithm.mapRound(amountRound, amountMatchInRound, i);
//            System.out.println("errorStrong: " + i.getTotalConstraintStrongReferee() + " errorReferee: " + i.getTotalConstraintReferee() + " errorSpace: " + i.getTotalConstraintSpace());
            for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
                System.out.println("---- " + entry.getKey() + " ----");
                int value[] = entry.getValue();
                for (int j = 0; j < value.length; j++)
                    System.out.print(value[j] + " ");
                System.out.println("----------");
            }
            System.out.println("FITNESS: " + i.getFitness());
        }
        System.out.println("fitnessBest: " + fitnessBest);

//        int test[] = {9 ,2, 11 ,7, 5 ,3 ,2, 1, 0 ,0};
//        System.out.println(geneticAlgorithm.constraintReferee(test, amountMatchInRound, amountTimeslotInday).getFitness());
    }
}


