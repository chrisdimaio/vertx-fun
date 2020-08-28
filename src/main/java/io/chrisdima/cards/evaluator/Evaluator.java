package io.chrisdima.cards.evaluator;

import org.apache.commons.math3.stat.Frequency;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class Evaluator {
    private static final List<Long> QUADS = Arrays.asList(1L, 4L);
    private static final List<Long> BOAT = Arrays.asList(2L, 3L);
    private static final List<Long> SET = Arrays.asList(3L, 1L, 1L);;
    private static final List<Long> TWO_PAIR = Arrays.asList(2L, 2L, 1L);

    public static void main( String[] args ){
        ArrayList<Card> quads = new ArrayList<>();
        quads.add(new Card(Rank.SIX, Suit.CLUBS));
        quads.add(new Card(Rank.FIVE, Suit.CLUBS));
        quads.add(new Card(Rank.FOUR, Suit.CLUBS));
        quads.add(new Card(Rank.THREE, Suit.CLUBS));
        quads.add(new Card(Rank.TWO, Suit.CLUBS));

        ArrayList<Card> boat = new ArrayList<>();
        boat.add(new Card(Rank.SIX, Suit.CLUBS));
        boat.add(new Card(Rank.SIX, Suit.DIAMONDS));
        boat.add(new Card(Rank.SIX, Suit.SPADES));
        boat.add(new Card(Rank.THREE, Suit.CLUBS));
        boat.add(new Card(Rank.THREE, Suit.SPADES));

        ArrayList<Card> highcard1 = new ArrayList<>();
        highcard1.add(new Card(Rank.TWO, Suit.CLUBS));
        highcard1.add(new Card(Rank.TWO, Suit.DIAMONDS));
        highcard1.add(new Card(Rank.FOUR, Suit.SPADES));
        highcard1.add(new Card(Rank.FOUR, Suit.CLUBS));
        highcard1.add(new Card(Rank.NINE, Suit.SPADES));

        ArrayList<Card> highcard2 = new ArrayList<>();
        highcard2.add(new Card(Rank.TWO, Suit.CLUBS));
        highcard2.add(new Card(Rank.THREE, Suit.DIAMONDS));
        highcard2.add(new Card(Rank.THREE, Suit.SPADES));
        highcard2.add(new Card(Rank.SIX, Suit.CLUBS));
        highcard2.add(new Card(Rank.SIX, Suit.HEARTS));

        Hand hand1 = createHand(highcard1);
        Hand hand2 = createHand(highcard2);
        System.out.println(compareHand(hand1, hand2));
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
                hand.setThreeOfAKind(true);
            }else if(hand.getCounts().equals(new ArrayList<>(TWO_PAIR))){
                hand.setTwoPair(true);
            }
        } else if (hand.getCounts().size() == 4) {
            hand.setOnePair(true);
        }
        hand.setFlush(cards.stream().map(Card::getSuit).distinct().limit(2).count() <= 1);
        hand.setStraight(cards.get(cards.size()-1).getRank() - cards.get(0).getRank() == 4);
        hand.setStraightFlush(hand.isStraight() && hand.isFlush());
        hand.setHighCard(!(hand.isQuads() || hand.isBoat() || hand.isThreeOfAKind() || hand.isTwoPair() || hand.isOnePair()
                || hand.isFlush()||hand.isStraight()||hand.isStraightFlush()));
        if(hand.isHighCard()) {
            hand.setHighestCard(cards.get(cards.size()-1));
        }
        hand.setHandValue(calcHandValue(hand));
//        System.out.println(hand.getHandValue());
        return hand;
    }
    /**
     * Returns a boolean indicating which hand wins
     * @param hand1
     * @param hand2
     * @return  true if hand1 beats hand2 else false
     */
    public static boolean compareHand(Hand hand1, Hand hand2){
        if(hand1.getHandValue() > hand2.getHandValue()){
            return true;
        } else if(hand1.getHandValue() < hand2.getHandValue()){
            return false;
        } else if(hand1.getHandValue() == hand2.getHandValue()){
            if(hand1.isHighCard()){
                return compareCard(hand1.getHighestCard(), hand2.getHighestCard());
            }
            if(hand1.isOnePair() || hand1.isTwoPair()){
                return comparePair(hand1, hand2);
            }
            if(hand1.isThreeOfAKind()){

            }
            if(hand1.isStraight()){

            }
            if(hand1.isFlush()){

            }
            if(hand1.isStraightFlush()){

            }
        }
        return false;
    }
    private static boolean comparePair(Hand hand1, Hand hand2){
        PairResult pairResult1 = getPairs(hand1);
        PairResult pairResult2 = getPairs(hand2);
        if(pairResult1.getPairSum() > pairResult2.getPairSum()){
            return true;
        } else if (pairResult1.getPairSum() < pairResult2.getPairSum()){
            return false;
        } else {
            if(pairResult1.getKickerSum() > pairResult2.getKickerSum())
                return true;
            return false;
        }
    }

    private static boolean compareCard(Card card1, Card card2){
        if(card1.getRank() > card2.getRank()){
            return true;
        } else if(card1.getRank() < card2.getRank()){
            return false;
        } else { // They are equal at this point.
            if(card1.getSuit() > card2.getSuit()){
                return true;
            } else {
                return false;
            }
        }
    }
    /**
     * Calculates a numerical value based off what the hand is by flipping th 0th to 8th
     * bits in a BitSet and converting the set to an int.
     * For example, a high card would flip
     * @param hand
     * @return
     */
    private static int calcHandValue(Hand hand){
        BitSet bits = new BitSet();
        bits.set(0, hand.isHighCard());
        bits.set(1, hand.isOnePair());
        bits.set(3, hand.isTwoPair());
        bits.set(4, hand.isThreeOfAKind());
        bits.set(5, hand.isStraight());
        bits.set(6, hand.isBoat());
        bits.set(7, hand.isQuads());
        bits.set(8, hand.isStraightFlush());
        return (int) bits.toLongArray()[0];
    }
    private static PairResult getPairs(Hand hand){
        PairResult result = new PairResult();
        hand.getHistogram().forEach((rank, frequency) ->{
            if(frequency == 2) {
                result.addPairRank(rank);
                result.setPairSum(result.getPairSum() + rank);
            }
            if(frequency == 1){
                result.setKickerSum(result.getKickerSum() + rank);
            }
        });
        return result;
    }
    private static HashMap<Integer, Long> getHistogram(ArrayList<Card> cards){
        Frequency frequency = new Frequency();
        cards.forEach(c -> frequency.addValue(c.getRank()));
        HashMap<Integer, Long> histogram = new HashMap<>();
        cards.forEach(c-> {
            histogram.put(c.getRank(), frequency.getCount(c.getRank()));
        });
//        System.out.println(histogram);
        return histogram;
    }
}