package io.chrisdima.cards;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class PlayerVerticle extends AbstractVerticle {
    private final String dealer_address;
    private final String table_address;

    public PlayerVerticle(String dealer_address, String table_address) {
        this.dealer_address = dealer_address;
        this.table_address = table_address;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        System.out.println("I'm a player");
        vertx.eventBus().request(this.dealer_address, "I'm ready to play!", ar->{
            if(ar.succeeded()){
                System.out.println("reply: " + ar.result().body());
            }
        });
        vertx.eventBus().consumer(this.table_address, message->{
            System.out.println("dealer: " + message.body());
        });
    }
}
