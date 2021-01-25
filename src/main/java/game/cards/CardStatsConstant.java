package game.cards;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.logging.Level;
import java.util.logging.Logger;

@Embeddable
public class CardStatsConstant implements CardStats{

    float approvalRating = 0;
    int cardPrint = 0;
    float dexterity = 0;
    int strength = 0;
    float wisdom = 0;
    @Enumerated(EnumType.ORDINAL)
    Charisma charisma = Charisma.NEUTRAL;

    public CardStatsConstant() {
    }

    public CardStatsConstant(float approvalRating, int cardPrint, float dexterity, int strength, float wisdom, Charisma charisma) {
        this.approvalRating = approvalRating;
        this.cardPrint = cardPrint;
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

    @Override
    public float getPowerLevel() {
        return approvalRating + cardPrint + dexterity + strength + wisdom;
    }

    @Override
    public float getApprovalRating() {
        return approvalRating;
    }

    public String getDescription() {
        return "charisma:" + charisma.getName() + " rating: " + approvalRating + " dex: " + dexterity + " str: " + strength + " wis: " + wisdom;
    }

}
