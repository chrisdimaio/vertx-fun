package io.chrisdima.cards.codecs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chrisdima.cards.PokerMessage;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.IOException;

public class PokerMessageCodec implements MessageCodec<PokerMessage, PokerMessage> {
    @Override
    public void encodeToWire(Buffer buffer, PokerMessage pokerMessage){
        try {
            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = mapper.writeValueAsBytes(pokerMessage);
            buffer.appendInt(bytes.length);
            buffer.appendBytes(bytes);
        } catch (JsonProcessingException jpe){
            throw new IllegalStateException("Error encoding JSON", jpe);
        }
    }

    @Override
    public PokerMessage decodeFromWire(int position, Buffer buffer){
        int length = buffer.getInt(position);
        byte[] bytes = buffer.getBytes(position + 4, position + 4 + length);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(bytes, PokerMessage.class);
        }catch (IOException ioe){
            throw new IllegalStateException("Error decoding JSON", ioe);
        }
    }

    @Override
    public PokerMessage transform(PokerMessage pokerMessage){
        return pokerMessage;
    }

    @Override
    public String name(){
        return "PokerMessageCodec";
    }

    @Override
    public byte systemCodecID(){
        return -1;
    }
}