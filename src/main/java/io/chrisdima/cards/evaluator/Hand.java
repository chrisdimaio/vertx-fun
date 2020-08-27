package io.chrisdima.cards.evaluator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public class Hand {
    private ArrayList<Card> cards;
    private HashMap<Integer, Long> histogram;
    private ArrayList<Long> counts;
    private int topCard;
    private boolean quads;
    private boolean boat;
    private boolean set;
    private boolean twoPair;
    private boolean onePair;
    private boolean flush;
    private boolean straight;
    private boolean straightFlush = flush && straight;
    private boolean highCard =
            !(quads || boat || set || twoPair || onePair || flush || straight || straightFlush);

    public Hand(ArrayList<Card> cards){
        this.cards = cards;
    }
}
