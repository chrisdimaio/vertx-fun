package io.chrisdima.cards;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;

import static io.chrisdima.cards.Messages.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

public class DealerVerticle extends AbstractVerticle {
    private static final String NAME = "DEALER";

    private static final int CARDS_IN_DECK = 52;
    private static final int CARDS_IN_HAND = 5;

    private final String dealerAddress;
    private final String tableAddress;
    private final int playerCount;

    private final ArrayList<Integer> deck = new ArrayList<>(52);

    private int activePlayers = 0;

    public DealerVerticle(String dealerAddress, String tableAddress, int playerCount){
        this.dealerAddress = dealerAddress;
        this.tableAddress = tableAddress;
        this.playerCount = playerCount;

        IntStream.iterate(0,i->i+1).limit(CARDS_IN_DECK).forEach(this.deck::add);
    }

    @Override
    public void start(Promise<Void> starPromise) {
        System.out.println("Dealer verticle is up!");
        vertx.eventBus().<PokerMessage>consumer(this.dealerAddress, message->{
            if(message.body().getCommand().equals(READY)) {
                sendReadyReply(message);
                this.activePlayers++;
            }
            if(this.activePlayers == this.playerCount){
                System.out.println("Table is ready to play!");
                Collections.shuffle(deck);
                this.deck.stream().limit(this.activePlayers * CARDS_IN_HAND).forEach(this::sendDealtCard);
                sendShowHand();
            }
        });
    }

    private void sendShowHand(){
        PokerMessage message = new PokerMessage();
        message.setSender(NAME);
        message.setCommand(SHOW_HAND);
        vertx.eventBus().publish(this.tableAddress, message);
    }

    private void sendReadyReply(Message<PokerMessage> originalMessage){
        PokerMessage message = new PokerMessage();
        message.setSender(NAME);
        message.setCommand(READY_REPLY);
        originalMessage.reply(message);
    }

    public void sendDealtCard(int card){
        PokerMessage message = new PokerMessage();
        message.setCommand(DEALT_CARD);
        message.setPayload(String.valueOf(card));
        message.setSender(NAME);
        vertx.eventBus().send(this.tableAddress, message);
    }
}
