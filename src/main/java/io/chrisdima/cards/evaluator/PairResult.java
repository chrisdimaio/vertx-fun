package io.chrisdima.cards.evaluator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


public class PairResult {
    @Getter @Setter
    private int kickerSum;

    @Getter @Setter
    private int pairSum;

    @Getter
    private ArrayList<Integer> pairRanks = new ArrayList<>();

    public void addPairRank(int pairRank){
        this.pairRanks.add(pairRank);
    }
}