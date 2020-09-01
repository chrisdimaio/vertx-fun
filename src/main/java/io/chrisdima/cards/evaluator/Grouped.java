package io.chrisdima.cards.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Grouped {

    private HashMap<Card, Group> groups = new HashMap<>();

    public Grouped(Hand hand){
        hand.getCards().forEach(card -> {
            if(groups.containsKey(card)){
                groups.get(card).increment();
            } else {
                groups.put(card, new Group(card));
            }
        });
    }

    private void add(Card card){

    }

    public Group get(Card card){
        return groups.get(card);
    }

    public ArrayList<Group> getSortedGroups(){
//        ArrayList<Card> cards = new ArrayList<>(this.groups.keySet());
////        System.out.println(this.groups.keySet());
//        cards.sort(Collections.reverseOrder());
//
//        ArrayList<Integer> sortedCounts = new ArrayList<>(this.groups.values());
//        sortedCounts.sort(Collections.reverseOrder());
        // NOTE: Sort the keys based off the values.
        ArrayList<Group> sortedGroups = new ArrayList<>(this.groups.values());
        sortedGroups.sort(Collections.reverseOrder());
        System.out.println(sortedGroups);
        return sortedGroups;
    }

//    @Override
    public boolean compareTo(Grouped other){
        ArrayList<Group> thisGroups = this.getSortedGroups();
        ArrayList<Group> otherGroups = other.getSortedGroups();
        for(int i=0; i < 5; i++){
            Group thisGroup = thisGroups.get(i);
            Group otherGroup = otherGroups.get(i);

            // The Hand that this group is derived from has already been determined to be the same as
            // the Hand 'other' has been derived from, therefore, we do not have to compare counts.
            if(thisGroup.getCount() > 1) {
                if (thisGroup.getCard().getRank() > otherGroup.getCard().getRank())
                    return true;
                if (thisGroup.getCard().getRank() < otherGroup.getCard().getRank())
                    return false;
            } else { // If card count is 1 it's a kicker, so compare rank & suit.
                System.out.println("here");
                return thisGroup.getCard().compareTo(otherGroup.getCard()) > 0;
            }
        }
        return false;
    }
}
