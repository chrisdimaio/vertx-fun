package io.chrisdima.cards.evaluator;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Card implements Comparable<Card>{
    @Setter
    private int rank;
    private final int suit;

    public Card(int rank, int suit){
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public int compareTo(Card other){
        if(this.rank != other.rank){
            return this.rank - other.rank;
        } else {
            return this.suit - other.suit;
        }
    }

    @Override
    public String toString(){
        return String.valueOf("[r: " + rank + " , s: " + suit + "]");
    }
}

