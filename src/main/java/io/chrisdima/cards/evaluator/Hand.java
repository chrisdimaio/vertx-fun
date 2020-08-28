package io.chrisdima.cards.evaluator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

@Getter @Setter
public class Hand {
    private ArrayList<Card> cards;
    private HashMap<Integer, Long> histogram;
    private ArrayList<Long> counts;
    private int handValue;
    private Card highestCard;
    private boolean quads;
    private boolean boat;
    private boolean threeOfAKind;
    private boolean twoPair;
    private boolean onePair;
    private boolean flush;
    private boolean straight;
    private boolean straightFlush;
    private boolean highCard;

    public Hand(ArrayList<Card> cards){
        System.out.println(cards);
        if(cards.size() == 5) {
            this.cards = cards;
            cards.sort(Comparator.comparing(Card::getRank));
        } else {
            throw new IllegalStateException("Requires 5 cards in hand.");
        }
    }
}
