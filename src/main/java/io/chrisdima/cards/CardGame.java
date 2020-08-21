package io.chrisdima.cards;

import io.vertx.core.Vertx;

public class CardGame {
    private static String TABLE_ADDRESS = "table";
    private static String DEALER_ADDRESS = "dealer";

    public static void main( String[] args )
    {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new DealerVerticle(DEALER_ADDRESS, TABLE_ADDRESS, 5));
        vertx.deployVerticle(new PlayerVerticle(DEALER_ADDRESS, TABLE_ADDRESS));
    }
}
