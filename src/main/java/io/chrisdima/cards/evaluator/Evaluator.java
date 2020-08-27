package io.chrisdima.cards.evaluator;

import org.apache.commons.math3.stat.Frequency;

import java.util.*;
import java.util.stream.Collectors;

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
        quads.add(new Card(Rank.SIX, Suit.CLUBS));
        quads.add(new Card(Rank.FIVE, Suit.CLUBS));
        quads.add(new Card(Rank.FOUR, Suit.CLUBS));
        quads.add(new Card(Rank.THREE, Suit.CLUBS));
        quads.add(new Card(Rank.TWO, Suit.CLUBS));

        Hand hand = createHand(quads);
    }
    public static Hand createHand(ArrayList<Card> cards){
        Hand hand = new Hand(cards);
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
        hand.setFlush(cards.stream().map(Card::getSuit).distinct().limit(2).count() <= 1);
        hand.setStraight(cards.get(cards.size()-1).getRank() - cards.get(0).getRank() == 4);
        hand.setStraightFlush(hand.isStraight() && hand.isFlush());
        hand.setHighCard(!(hand.isQuads() || hand.isBoat() || hand.isSet() || hand.isTwoPair() || hand.isOnePair()
                || hand.isFlush()||hand.isStraight()||hand.isStraightFlush()));
        if(hand.isHighCard()) {
            hand.setHighestCard(cards.get(cards.size()-1));
        }
        System.out.println(hand.getHighestCard());
        return hand;
    }

    /**
     * Returns a boolean indicating which hand wins
     * @param hand1
     * @param hand2
     * @return  true if hand1 beats hand2 else false
     */
    public static boolean compare(Hand hand1, Hand hand2){
        if(hand1.isHighCard()){
            if(hand2.isHighCard()){
                if(hand1.getHighestCard().getRank() > hand2.getHighestCard().getRank()){
                    return true;
                } else if(hand1.getHighestCard().getRank() == hand2.getHighestCard().getRank()){
                    return hand1.getHighestCard().getSuit() > hand2.getHighestCard().getSuit();
                }
            }
            return false;
        }
        if(hand1.isOnePair()){
            if(hand2.isHighCard()){
                return true;
            }
            if(hand2.isOnePair()){
                // hand1 get pair
                // hand2 get pair
                System.out.println(hand1.getHistogram());
            }
            return false;
        }
        return false;
    }
    private static Map<Integer, Long> getPairs(Hand hand){
        return hand.getHistogram().entrySet()
                .stream()
                .filter(map -> map.getValue() == 2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
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