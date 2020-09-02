package io.chrisdima.cards.codecs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chrisdima.cards.evaluator.Card;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.IOException;

public class CardCodec implements MessageCodec<Card, Card> {
    @Override
    public void encodeToWire(Buffer buffer, Card card){
        try {
            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = mapper.writeValueAsBytes(card);
            buffer.appendInt(bytes.length);
            buffer.appendBytes(bytes);
        } catch (JsonProcessingException jpe){
            throw new IllegalStateException("Error encoding JSON", jpe);
        }
    }

    @Override
    public Card decodeFromWire(int position, Buffer buffer){
        int length = buffer.getInt(position);
        byte[] bytes = buffer.getBytes(position + 4, position + 4 + length);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(bytes, Card.class);
        }catch (IOException ioe){
            throw new IllegalStateException("Error decoding JSON", ioe);
        }
    }

    @Override
    public Card transform(Card card){
        return card;
    }

    @Override
    public String name(){
        return "CardCodec";
    }

    @Override
    public byte systemCodecID(){
        return -1;
    }
}