package io.chrisdima.cards;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import static io.chrisdima.cards.Messages.*;

public class DealerVerticle extends AbstractVerticle {
    private final String dealerAddress;
    private final String tableAddress;
    private final int playerCount;

    private int activePlayers = 0;

    public DealerVerticle(String dealerAddress, String tableAddress, int playerCount){
        this.dealerAddress = dealerAddress;
        this.tableAddress = tableAddress;
        this.playerCount = playerCount;
    }

    @Override
    public void start(Promise<Void> starPromise) {
        System.out.println("Dealer verticle is up!");
        vertx.eventBus().consumer(this.dealerAddress, message->{
            if(message.body() == READY) {
                message.reply(READY_REPLY);
                this.activePlayers++;
            }
            if(this.activePlayers == this.playerCount){
                System.out.println("table is ready to play!");
                dealerACard();
                dealerACard();
                dealerACard();
                dealerACard();
                dealerACard();
                vertx.eventBus().publish(this.tableAddress, SHOW_HAND);
            }
        });
    }

    public void dealerACard(){
        for (int i = 0; i < this.playerCount; i++) {
            vertx.eventBus().send(this.tableAddress, i);
        }
    }
}
