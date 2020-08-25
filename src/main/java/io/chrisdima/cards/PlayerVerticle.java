package io.chrisdima.cards;

import io.chrisdima.cards.codecs.PokerMessage;
import io.chrisdima.cards.codecs.PokerMessageCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.util.ArrayList;

import static io.chrisdima.cards.Messages.READY;
import static io.chrisdima.cards.Messages.SHOW_HAND;

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
        vertx.eventBus().registerDefaultCodec(PokerMessage.class, new PokerMessageCodec());
        sendReady();
        vertx.eventBus().consumer(this.table_address, message->{
            if(this.hand.size() < 5){
                this.hand.add((int)message.body());
            }
            if(message.body() == SHOW_HAND) {
                System.out.println(name + "'s hand: " + this.hand);
            }
        });
    }

    private void sendReady(){
        PokerMessage message = new PokerMessage();
        message.setSender(this.name);
        message.setCommand(READY);
        vertx.eventBus().request(this.dealer_address, message, ar->{
            if(ar.succeeded()){
                System.out.println("reply: " + ar.result().body());
            }
            if(ar.failed()){
                System.out.println("failed");
            }
        });
    }
}
