package game.cards;

import game.CharismaState;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CardStats implements Cloneable {
    float dexterity;            // how quickly the card was picked up
    float intelligence;         // What condition the card was dropped in
    float defenseLevel;         // The level of armor the card has on
    CharismaState charisma;     // the card has a dye or frame or not

    public CardStats() {
        this(0, 10, 0, CharismaState.NEUTRAL);
    }

    public CardStats(float dexterity, float intelligence, float defenseLevel, CharismaState charisma) {
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.defenseLevel = defenseLevel;
        this.charisma = charisma;
    }

    @Override
    public CardStats clone() {
        try {
            return (CardStats) super.clone();
        } catch (CloneNotSupportedException e) {
            Logger.getGlobal().log(Level.SEVERE, "cannot clone stats object");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardStats){
            CardStats other = (CardStats) obj;
            return dexterity == other.dexterity &&
                    intelligence == other.intelligence &&
                    defenseLevel == other.defenseLevel &&
                    charisma == other.charisma;
        }else{
            return false;
        }
    }

}
