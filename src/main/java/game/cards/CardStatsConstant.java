package game.cards;

import game.Charisma;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CardStatsConstant{

    float approvalRating;
    float dexterity;
    int strength;
    float wisdom;
    Charisma charisma;

    public CardStatsConstant(float approvalRating, float dexterity, int strength, float wisdom, Charisma charisma) {
        this.approvalRating = approvalRating;
        this.dexterity = dexterity;
        this.strength = strength;
        this.wisdom = wisdom;
        this.charisma = charisma;
    }

    @Override
    public CardStatsConstant clone() {
        try {
            return (CardStatsConstant) super.clone();
        } catch (CloneNotSupportedException e) {
            Logger.getGlobal().log(Level.SEVERE, "cannot clone stats object");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardStatsConstant){
            CardStatsConstant other = (CardStatsConstant) obj;
            return approvalRating == other.approvalRating &&
                    dexterity == other.dexterity &&
                    strength == other.strength &&
                    wisdom == other.wisdom &&
                    charisma == other.charisma;
        }else{
            return false;
        }
    }
}
