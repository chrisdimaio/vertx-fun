package io.chrisdima.cards;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class DealerVerticle extends AbstractVerticle {
    private final String dealer_address;
    private final String table_address;

    public DealerVerticle(String dealer_address, String table_address, Integer playerCount){
        this.dealer_address = dealer_address;
        this.table_address = table_address;
    }

    @Override
    public void start(Promise<Void> starPromise) {
        System.out.println("Dealer verticle is up!");

        vertx.eventBus().consumer(this.dealer_address, message->{
           System.out.println("message: " + message.body());
        });
    }
}
