package io.chrisdima.cards;

import io.chrisdima.cards.evaluator.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;

import javax.xml.stream.events.Characters;

import static io.chrisdima.cards.Messages.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DealerVerticle extends AbstractVerticle {
    private static final String NAME = "DEALER";

    private static final int CARDS_IN_HAND = 5;

    private final String dealerAddress;
    private final String tableAddress;
    private final int playerCount;

    private final ArrayList<Card> deck;

    private int activePlayers = 0;

    public DealerVerticle(String dealerAddress, String tableAddress, int playerCount){
        this.dealerAddress = dealerAddress;
        this.tableAddress = tableAddress;
        this.playerCount = playerCount;

        deck = Deck.getDeck();
    }

    @Override
    public void start(Promise<Void> starPromise) {
        System.out.println("Dealer verticle is up!");
        HashMap<Hand, String> table = new HashMap<>();

        vertx.eventBus().<PokerMessage>consumer(this.dealerAddress, message->{
            if(message.body().getCommand().equals(READY)) {
                sendReadyReply(message);
                this.activePlayers++;
                if(this.activePlayers == this.playerCount){
                    System.out.println("Table is ready to play!");
                    Collections.shuffle(deck);
                    this.deck
                            .stream()
                            .limit(this.activePlayers * CARDS_IN_HAND)
                            .forEach(this::sendDealtCard);
                    sendShowHand();
                }
            } else if (message.body().getCommand().equals(MY_HAND)) {
                table.put(message.body().getHand(), message.body().getSender());
            }
            if(table.size() == 5){
                Hand winner = Evaluator.winner(new ArrayList<>(table.keySet()));
                for (Hand hand : table.keySet()) {
                    System.out.println(table.get(hand) + ": " + hand);
                }
                System.out.println(table.get(winner) + " wins with: " + winner);
            }

        });
    }

    private void sendShowHand(){
        PokerMessage message = new PokerMessage();
        message.setSender(NAME);
        message.setCommand(SHOW_HAND);
        vertx.eventBus().publish(this.tableAddress, message);
    }

    private void sendReadyReply(Message<PokerMessage> originalMessage){
        PokerMessage message = new PokerMessage();
        message.setSender(NAME);
        message.setCommand(READY_REPLY);
        originalMessage.reply(message);
    }

    public void sendDealtCard(Card card){
        PokerMessage message = new PokerMessage();
        message.setCommand(DEALT_CARD);
        message.setPayload(card);
        message.setSender(NAME);
        vertx.eventBus().send(this.tableAddress, message);
    }
}
