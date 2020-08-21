package io.chrisdima.cards;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class PlayerVerticle extends AbstractVerticle {
    private final String address;

    public PlayerVerticle(String address) {
        this.address = address;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().publish(this.address, "Hello");
    }
}
