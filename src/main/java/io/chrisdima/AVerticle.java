package io.chrisdima;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class AVerticle extends AbstractVerticle {
    private String id;

    public AVerticle(String id){
        this.id = id;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        System.out.println("verticle: " + this.id);
    }

    @Override
    public void stop(Promise stopPromise) throws Exception {
        System.out.println("AVerticle stopped!");
    }
}
