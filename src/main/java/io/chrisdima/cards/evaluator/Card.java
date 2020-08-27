package io.chrisdima.cards.evaluator;

import lombok.Getter;
import lombok.Setter;
@Getter
public class Card {
    private int rank;
    private int suit;

    public Card(int rank, int suit){
        this.rank = rank;
        this.suit = suit;
    }
}

