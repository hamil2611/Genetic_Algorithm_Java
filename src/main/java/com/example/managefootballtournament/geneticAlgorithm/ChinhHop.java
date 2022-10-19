package com.example.managefootballtournament.geneticAlgorithm;

import com.example.managefootballtournament.entity.FootballTeam;
import com.example.managefootballtournament.entity.Match;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class ChinhHop {
    int n,count;
    int[] b = new int[20];
    int[] p = new int[20];
    Map<Integer, Integer> mapChinhHop = new HashMap<>();

    public void init(int n) {
        for(int i=1;i<=n;i++) b[i]=1;
    }

    public void SolveChan(int i) {
        System.out.println(i);
        for(int j=1;j<=n;j++)
            if(b[j]==n) {
                p[i]=j;
                b[j]=0;
                if(i==2){
                    mapChinhHop.put(p[1],p[2]);
                    System.out.println(p[1]+"----"+p[2]);
                }
                else
                    SolveChan(i+1);
                b[j]=1;
            }
    }

    public void SolveLe(int i) {
        for(int j=0;j<=n;j++)
            if(b[j]==1) {
                p[i]=j;
                b[j]=0;
                if(i==2){
                    mapChinhHop.put(p[1],p[2]);

                }
                else
                    SolveLe(i+1);
                b[j]=1;
            }
    }

    public List<Match> mainSolve(List<FootballTeam> footballTeamList, int amountTeam){
        List<Match> matchList = new ArrayList<>();
        init(amountTeam);
        SolveChan(1);
        System.out.println(mapChinhHop+"-----");
        return matchList;
    }


}
