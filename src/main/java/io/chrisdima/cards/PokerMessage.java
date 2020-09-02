package io.chrisdima.cards;

import io.chrisdima.cards.evaluator.Card;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class PokerMessage implements Serializable {
    private String command;
    private Card payload;
    private String sender;
}