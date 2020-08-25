package io.chrisdima.cards;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class PokerMessage implements Serializable {
    private String command;
    private String payload;
    private String sender;
}