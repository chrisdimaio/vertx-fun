package io.chrisdima.cards;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class Dealer extends AbstractVerticle {
    @Override
    public void start(Promise<Void> starPromise) {
        vertx.eventBus().localConsumer("card_table", message->{
           System.out.println(message.body());
        });
    }

//    @Override
//    public void stop(Promise<Void> stopPromise) {
//
//    }
}
