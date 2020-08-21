package io.chrisdima.cards;

import io.vertx.core.Vertx;

public class CardGame {
    public static void main( String[] args )
    {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new DealerVerticle(), r->{
            System.out.println("Dealer");
        });
    }
}
