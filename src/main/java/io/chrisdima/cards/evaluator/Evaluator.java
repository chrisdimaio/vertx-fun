package io.chrisdima.cards.evaluator;

import org.apache.commons.math3.stat.Frequency;

import java.util.*;

public class Evaluator {
    private static final List<Long> QUADS = Arrays.asList(1L, 4L);
    private static final List<Long> BOAT = Arrays.asList(2L, 3L);
    private static final List<Long> SET = Arrays.asList(3L, 1L, 1L);;
    private static final List<Long> TWO_PAIR = Arrays.asList(2L, 2L, 1L);

    public static void main( String[] args ){
//        String[] quads = {"3", "3", "3", "3", "K"};
//        String[] boat = {"3", "3", "3", "2", "2"};
//        String[] set = {"3", "3", "3", "4", "5"};
//        String[] twoPair = {"3", "3", "4", "4", "K"};
//        String[] onePair = {"3", "3", "4", "5", "6"};

        ArrayList<Card> quads = new ArrayList<>();
        quads.add(new Card(Rank.THREE, Suit.CLUBS));
        quads.add(new Card(Rank.THREE, Suit.CLUBS));
        quads.add(new Card(Rank.THREE, Suit.CLUBS));
        quads.add(new Card(Rank.THREE, Suit.CLUBS));
        quads.add(new Card(Rank.KING, Suit.CLUBS));

        Hand hand = createHand(quads);
    }
    public static Hand createHand(ArrayList<Card> cards){
        Hand hand = new Hand(cards);
        hand.setCards(cards);
        hand.setHistogram(getHistogram(cards));
        ArrayList<Long> counts = new ArrayList<>(hand.getHistogram().values());
        Collections.sort(counts);
        hand.setCounts(counts);
        if(hand.getCounts().size() == 2){
            if(hand.getCounts().equals(new ArrayList<>(QUADS))){
                hand.setQuads(true);
            } else if(hand.getCounts().equals(new ArrayList<>(BOAT))){
                hand.setBoat(true);
            }
        } else if(hand.getCounts().size() == 3){
            if(hand.getCounts().equals(new ArrayList<>(SET))){
                hand.setSet(true);
            }else if(hand.getCounts().equals(new ArrayList<>(TWO_PAIR))){
                hand.setTwoPair(true);
            }
        } else if (hand.getCounts().size() == 4) {
            hand.setOnePair(true);
        }
        return hand;
    }
    public static boolean compare(Hand hand1, Hand Hand2){
        return false;
    }
//    private static compareCounts()
    private static HashMap<Integer, Long> getHistogram(ArrayList<Card> cards){
        Frequency frequency = new Frequency();
        cards.forEach(c -> frequency.addValue(c.getRank()));
        HashMap<Integer, Long> histogram = new HashMap<>();
        cards.forEach(c-> {
            histogram.put(c.getRank(), frequency.getCount(c.getRank()));
        });
        System.out.println(histogram);
        return histogram;
    }
}