package io.chrisdima.cards;

import io.chrisdima.poker.evaluator.Card;
import io.chrisdima.poker.evaluator.Evaluator;
import io.chrisdima.poker.evaluator.Hand;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.util.ArrayList;

import static io.chrisdima.cards.Messages.*;

public class PlayerVerticle extends AbstractVerticle {
    private final String name;
    private final String dealer_address;
    private final String table_address;

    private Hand hand;

    private ArrayList<Card> cards = new ArrayList<>();

    public PlayerVerticle(String dealer_address, String table_address, int id) {
        this.name = "Player #" + id;
        this.dealer_address = dealer_address;
        this.table_address = table_address;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        System.out.println("I'm a player");
        sendReady();
        vertx.eventBus().<PokerMessage>consumer(this.table_address, message->{
            if(this.cards.size() < 5 && message.body().getCommand().equals(DEALT_CARD)){
                this.cards.add(message.body().getPayload());
            } else {
                hand = Evaluator.createHand(cards);
            }
            if(message.body().getCommand().equals(SHOW_HAND)) {
                PokerMessage pokerMessage = new PokerMessage();
                pokerMessage.setCommand(MY_HAND);
                pokerMessage.setHand(hand);
                pokerMessage.setSender(name);
                vertx.eventBus().send(this.dealer_address, pokerMessage);
            }
        });
    }

    private void sendReady(){
        PokerMessage message = new PokerMessage();
        message.setSender(this.name);
        message.setCommand(READY);
        vertx.eventBus().<PokerMessage>request(this.dealer_address, message, ar->{
            if(ar.succeeded()){
                System.out.println("Reply: " + ar.result().body().getCommand());
            }
            if(ar.failed()){
                System.out.println("Failed");
            }
        });
    }
}
