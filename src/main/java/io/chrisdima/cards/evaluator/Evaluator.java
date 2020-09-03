package io.chrisdima.cards.evaluator;

import org.apache.commons.math3.stat.Frequency;

import java.util.*;

public class Evaluator {
    private static final List<Long> QUADS = Arrays.asList(1L, 4L);
    private static final List<Long> BOAT = Arrays.asList(2L, 3L);
    private static final List<Long> THREE_OF_KIND = Arrays.asList(3L, 1L, 1L);
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
        highcard1.add(new Card(Rank.TEN, Suit.CLUBS));
        highcard1.add(new Card(Rank.JACK, Suit.DIAMONDS));
        highcard1.add(new Card(Rank.QUEEN, Suit.SPADES));
        highcard1.add(new Card(Rank.KING, Suit.CLUBS));
        highcard1.add(new Card(Rank.ACE, Suit.SPADES));

        ArrayList<Card> highcard2 = new ArrayList<>();
        highcard2.add(new Card(Rank.ACE, Suit.CLUBS));
        highcard2.add(new Card(Rank.TWO, Suit.DIAMONDS));
        highcard2.add(new Card(Rank.THREE, Suit.SPADES));
        highcard2.add(new Card(Rank.FOUR, Suit.CLUBS));
        highcard2.add(new Card(Rank.FIVE, Suit.HEARTS));

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
            if(hand.getCounts().equals(new ArrayList<>(THREE_OF_KIND))){
                hand.setThreeOfAKind(true);
            }else if(hand.getCounts().equals(new ArrayList<>(TWO_PAIR))){
                hand.setTwoPair(true);
            }
        } else if (hand.getCounts().size() == 4) {
            hand.setOnePair(true);
        }
        hand.setFlush(cards.stream().map(Card::getSuit).distinct().limit(2).count() <= 1);
        hand.setStraight(testForStraight(hand));
        hand.setStraightFlush(hand.isStraight() && hand.isFlush());
        hand.setHighCard(!(hand.isQuads() || hand.isBoat() || hand.isThreeOfAKind() || hand.isTwoPair() || hand.isOnePair()
                || hand.isFlush()||hand.isStraight()||hand.isStraightFlush()));
        if(hand.isHighCard()) {
            hand.setHighestCard(cards.get(cards.size()-1));
        }
        hand.setHandValue(calcHandValue(hand));
        return hand;
    }
    /**
     * Returns a boolean indicating which hand wins
     * @param hand1 Hand that is doing the compare.
     * @param hand2 Hand that is being compared to.
     * @return  true if hand1 beats hand2 else false.
     */
    public static boolean compareHand(Hand hand1, Hand hand2){
        if(hand1.getHandValue() > hand2.getHandValue()){
            return true;
        } else if(hand1.getHandValue() < hand2.getHandValue()){
            return false;
        } else if(hand1.getHandValue() == hand2.getHandValue()){
            Grouped grouped1 = new Grouped(hand1);
            Grouped grouped2 = new Grouped(hand2);
            return grouped1.compare(grouped2);
        }
        return false;
    }

    private static boolean testForStraight(Hand hand){
        ArrayList<Card> cards = hand.getCards();
        boolean highStraight =
                cards.get(0).getRank() - cards.get(4).getRank()
                == 4;
        // If cards contain an Ace and it's not a high straight test for Ace high and low straight.
        if(!highStraight && hand.containsByRank(Rank.ACE)){
            cards.get(0).setRank(1);
            cards.sort(Collections.reverseOrder());
            boolean lowStraight =
                    cards.get(0).getRank() - cards.get(cards.size()-1).getRank()
                    == 4;

            // If we didn't find a low straight put rank back.
            if(lowStraight){
                return true;
            } else
                cards.get(4).setRank(1);
            cards.sort(Collections.reverseOrder());;

        }
        return highStraight;
    }

    /**
     * Calculates a numerical value based off what the hand is by flipping th 0th to 8th
     * bits in a BitSet and converting the set to an int. The higher the value the better.
     * @param hand Hand to calculate value of.
     * @return value of hand.
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

    // Can probably modify Grouped to handle this.
    private static HashMap<Integer, Long> getHistogram(ArrayList<Card> cards){
        Frequency frequency = new Frequency();
        cards.forEach(c -> frequency.addValue(c.getRank()));
        HashMap<Integer, Long> histogram = new HashMap<>();
        cards.forEach(c-> histogram.put(c.getRank(), frequency.getCount(c.getRank())));
        return histogram;
    }
}