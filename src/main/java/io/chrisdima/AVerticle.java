package io.chrisdima;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class AVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startFuture) {
        System.out.println("AVerticle started!");
    }

    @Override
    public void stop(Promise stopFuture) throws Exception {
        System.out.println("AVerticle stopped!");
    }
}
