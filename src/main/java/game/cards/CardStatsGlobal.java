package game.cards;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class CardStatsGlobal implements CardStats {

    int amountCardsPrinted;
    int amountCardsBurned;
    int cardFightsTotal;
    Charisma charisma;

    public CardStatsGlobal() {
        this(0, 0, 0, Charisma.fromValue(0));
    }

    public CardStatsGlobal(int amountCardsPrinted, int amountCardsBurned, int cardFightsTotal, Charisma charisma) {
        this.amountCardsPrinted = amountCardsPrinted;
        this.amountCardsBurned = amountCardsBurned;
        this.cardFightsTotal = cardFightsTotal;
        this.charisma = charisma;
    }

    public float calcApprovalRating(){
        if (getAmountCardsBurned() != 0){
            return (getAmountCardsPrinted() - getAmountCardsBurned()) / (float) getAmountCardsBurned();
        }else{
            return getAmountCardsPrinted() - getAmountCardsBurned();
        }
    }

    private float wisdomFunction(int amountCardsPrinted) {
        return amountCardsPrinted;
    }

    public CardStatsConstant getStatsForPickDelay(float delayCardPicked) {
        return new CardStatsConstant(
                calcApprovalRating(),
                amountCardsPrinted,
                delayCardPicked,
                cardFightsTotal,
                wisdomFunction(amountCardsPrinted),
                charisma
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardStatsGlobal){
            CardStatsGlobal other = (CardStatsGlobal) obj;
            return amountCardsPrinted == other.amountCardsPrinted &&
                    amountCardsBurned == other.amountCardsBurned &&
                    cardFightsTotal == other.cardFightsTotal &&
                    charisma == other.charisma;
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

    public void setCharisma(Charisma charisma) {
        this.charisma = charisma;
    }

    @Transient
    @Override
    public float getPowerLevel() {
        return getStatsForPickDelay(0).getPowerLevel();
    }

    @Transient
    @Override
    public float getApprovalRating() {
        return calcApprovalRating();
    }
}
