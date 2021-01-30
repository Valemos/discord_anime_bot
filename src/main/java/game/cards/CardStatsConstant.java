package game.cards;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.logging.Level;
import java.util.logging.Logger;

@Embeddable
public class CardStatsConstant implements CardStats{

    float approvalRating = 0;
    int cardPrint = 0;
    float dexterity = 0;
    float strength = 0;
    float wisdom = 0;

    public CardStatsConstant() {
    }

    public CardStatsConstant(float approvalRating, int cardPrint, float dexterity, float strength, float wisdom) {
        this.approvalRating = approvalRating;
        this.cardPrint = cardPrint;
        this.dexterity = dexterity;
        this.strength = strength;
        this.wisdom = wisdom;
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
                    cardPrint == other.cardPrint &&
                    dexterity == other.dexterity &&
                    strength == other.strength &&
                    wisdom == other.wisdom;
        }else{
            return false;
        }
    }

    @Transient
    @Override
    public float getPowerLevel() {
        return dexterity + strength + wisdom;
    }

    @Transient
    @Override
    public float getApprovalRating() {
        return approvalRating;
    }

    @Transient
    public String getDescription() {
        return " rating: " + approvalRating + " dex: " + dexterity + " str: " + strength + " wis: " + wisdom;
    }

}
