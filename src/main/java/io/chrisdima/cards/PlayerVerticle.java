package io.chrisdima.cards;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.util.ArrayList;
import java.util.List;

import static io.chrisdima.cards.Messages.READY;

public class PlayerVerticle extends AbstractVerticle {
    private final int id;
    private final String dealer_address;
    private final String table_address;

    private ArrayList<Integer> hand = new ArrayList<>();

    public PlayerVerticle(String dealer_address, String table_address, int id) {
        this.id = id;
        this.dealer_address = dealer_address;
        this.table_address = table_address;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        System.out.println("I'm a player");
        vertx.eventBus().request(this.dealer_address, READY, ar->{
            if(ar.succeeded()){
                System.out.println("reply: " + ar.result().body());
            }
            if(ar.failed()){
                System.out.println("failed");
            }
        });
        vertx.eventBus().consumer(this.table_address, message->{
            if(this.hand.size() < 5){
                this.hand.add((int)message.body());
            }
            System.out.println("player " + id + "'s hand: " + this.hand);
        });
    }
}
