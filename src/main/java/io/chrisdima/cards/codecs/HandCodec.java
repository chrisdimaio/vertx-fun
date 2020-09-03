package io.chrisdima.cards.codecs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chrisdima.cards.evaluator.Hand;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.IOException;

public class HandCodec implements MessageCodec<Hand, Hand> {
    @Override
    public void encodeToWire(Buffer buffer, Hand hand){
        try {
            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = mapper.writeValueAsBytes(hand);
            buffer.appendInt(bytes.length);
            buffer.appendBytes(bytes);
        } catch (JsonProcessingException jpe){
            throw new IllegalStateException("Error encoding JSON", jpe);
        }
    }

    @Override
    public Hand decodeFromWire(int position, Buffer buffer){
        int length = buffer.getInt(position);
        byte[] bytes = buffer.getBytes(position + 4, position + 4 + length);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(bytes, Hand.class);
        }catch (IOException ioe){
            throw new IllegalStateException("Error decoding JSON", ioe);
        }
    }

    @Override
    public Hand transform(Hand hand){
        return hand;
    }

    @Override
    public String name(){
        return "HandCodec";
    }

    @Override
    public byte systemCodecID(){
        return -1;
    }
}