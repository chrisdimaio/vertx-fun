package io.chrisdima.cards;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.util.ArrayList;

import static io.chrisdima.cards.Messages.*;

public class PlayerVerticle extends AbstractVerticle {
    private final String name;
    private final String dealer_address;
    private final String table_address;

    private ArrayList<Integer> hand = new ArrayList<>();

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
            if(this.hand.size() < 5 && message.body().getCommand().equals(DEALT_CARD)){
                this.hand.add(Integer.valueOf(message.body().getPayload()));
            }
            if(message.body().getCommand().equals(SHOW_HAND)) {
                System.out.println(name + "'s hand: " + this.hand);
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
