package io.chrisdima.cards.evaluator;

import lombok.Getter;
import lombok.Setter;
@Getter
public class Card implements Comparable<Card>{
    private int rank;
    private int suit;

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
    public int hashCode(){
        return Integer.parseInt(String.valueOf(this.rank) + String.valueOf(this.suit));
    }

    @Override
    public String toString(){
        return String.valueOf(rank);
    }
}

