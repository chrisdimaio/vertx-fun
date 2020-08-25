package io.chrisdima.cards;

import io.chrisdima.cards.codecs.PokerMessageCodec;
import io.vertx.core.Vertx;

public class CardGame {
    private static String TABLE_ADDRESS = "table";
    private static String DEALER_ADDRESS = "dealer";

    public static void main( String[] args )
    {
        Vertx vertx = Vertx.vertx();
        vertx.eventBus().registerDefaultCodec(PokerMessage.class, new PokerMessageCodec());

        vertx.deployVerticle(new DealerVerticle(DEALER_ADDRESS, TABLE_ADDRESS, 5));
        for (int i = 0; i < 5; i++) {
            vertx.deployVerticle(new PlayerVerticle(DEALER_ADDRESS, TABLE_ADDRESS, i));
        }
    }
}
