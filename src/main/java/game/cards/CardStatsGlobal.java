package game.cards;

import org.apache.commons.math3.util.Precision;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class CardStatsGlobal implements CardStats {

    int amountCardsPrinted;
    int amountCardsBurned;
    int cardFightsTotal;

    @Transient
    private static final CardStatsConstant baseStats = new CardStatsConstant(30, 50, 50, 0, 0);

    @Transient
    public static final int wisdomHalfLife = 1000;

    public CardStatsGlobal() {
        this(0, 0, 0);
    }

    public CardStatsGlobal(int amountCardsPrinted, int amountCardsBurned, int cardFightsTotal) {
        this.amountCardsPrinted = amountCardsPrinted;
        this.amountCardsBurned = amountCardsBurned;
        this.cardFightsTotal = cardFightsTotal;
    }

    public CardStatsConstant getConstantStats(CardPickInfo pickInfo) {
        return new CardStatsConstant(
                getDexterity(baseStats.dexterity, pickInfo.getPickDelay()), getStrength(baseStats.strength, pickInfo.getCardFights()), getWisdom(baseStats.wisdom, amountCardsPrinted), getApprovalRating(),
                amountCardsPrinted
        );
    }

    /*
    To calculate wisdom, we use base wisdom stat for the first card printed,
    and by the time reaching "wisdomHalfLife" number of printed cards,
    wisdom of this card will become half of base value
     */
    protected float getWisdom(float base, int cardPrint) {
        if (cardPrint < 1){
            return base;
        }
        return (float) (Precision.round(base / Math.pow(2, cardPrint / (float)wisdomHalfLife), 1));
    }

    protected float getStrength(float base, int cardFights) {
        if      (cardFights >= 4)   return base;
        else if (cardFights == 3)   return base * 0.75f;
        else if (cardFights == 2)   return base * 0.5f;
        else if (cardFights == 1)   return base * 0.25f;
        else                        return 0;
    }

    protected float getDexterity(float base, float delay) {
        if      (delay <= 1)    return base;
        else if (delay <= 3)    return base * 0.75f;
        else if (delay <= 6)    return base * 0.5f;
        else if (delay <= 10)   return base * 0.25f;
        else                    return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardStatsGlobal){
            CardStatsGlobal other = (CardStatsGlobal) obj;
            return amountCardsPrinted == other.amountCardsPrinted &&
                    amountCardsBurned == other.amountCardsBurned &&
                    cardFightsTotal == other.cardFightsTotal;
        }else{
            return false;
        }
    }

    public void incrementCardPrint() {
        amountCardsPrinted++;
    }

    public void incrementCardFights() {
        cardFightsTotal++;
    }

    public void incrementCardBurned() {
        amountCardsBurned++;
    }

    public int getAmountCardsPrinted() {
        return amountCardsPrinted;
    }

    public void setAmountCardsPrinted(int amountCardsPrinted) {
        this.amountCardsPrinted = amountCardsPrinted;
    }

    public int getAmountCardsBurned() {
        return amountCardsBurned;
    }

    public void setAmountCardsBurned(int amountCardsBurned) {
        this.amountCardsBurned = amountCardsBurned;
    }

    public int getCardFightsTotal() {
        return cardFightsTotal;
    }

    public void setCardFightsTotal(int cardFightsTotal) {
        this.cardFightsTotal = cardFightsTotal;
    }

    @Transient
    @Override
    public float getPowerLevel() {
        return getConstantStats(new CardPickInfo(1, 1)).getPowerLevel();
    }

    @Transient
    @Override
    public float getApprovalRating() {
        if (getAmountCardsBurned() != 0){
            return (getAmountCardsPrinted() - getAmountCardsBurned()) / (float) getAmountCardsBurned();
        }else{
            return getAmountCardsPrinted() - getAmountCardsBurned();
        }
    }

    @Override
    @Transient
    public String getDescription() {
        return "printed: " + amountCardsPrinted +
                " burned: " + amountCardsBurned +
                " total fights: " + cardFightsTotal;
    }
}
