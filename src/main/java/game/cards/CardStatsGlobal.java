package game.cards;

import game.Charisma;
import org.jetbrains.annotations.NotNull;

public class CardStatsGlobal implements Comparable<CardStatsGlobal> {

    int cardId;
    int amountCardsPrinted;
    int amountCardsBurned;
    int cardFightsTotal;
    Charisma charisma;


    public CardStatsGlobal() {
        this(-1, 0, 0, 0, Charisma.fromValue(0));
    }

    public CardStatsGlobal(int cardId, int amountCardsPrinted, int amountCardsBurned, int cardFightsTotal, Charisma charisma) {
        this.cardId = cardId;
        this.amountCardsPrinted = amountCardsPrinted;
        this.amountCardsBurned = amountCardsBurned;
        this.cardFightsTotal = cardFightsTotal;
        this.charisma = charisma;
    }

    public float getApprovalRating(){
        if (amountCardsBurned != 0){
            return (amountCardsPrinted - amountCardsBurned) / (float) amountCardsBurned;
        }else{
            return amountCardsPrinted - amountCardsBurned;
        }
    }

    public int getStrength() {
        return cardFightsTotal;
    }

    public float getWisdom() {
        return wisdomFunction(amountCardsPrinted);
    }

    private float wisdomFunction(int amountCardsPrinted) {
        return amountCardsPrinted;
    }

    public Charisma getCharisma() {
        return charisma;
    }

    public CardStatsConstant getStatsForPickDelay(float delayCardPicked) {
        return new CardStatsConstant(
                getApprovalRating(),
                amountCardsPrinted,
                delayCardPicked,
                getStrength(),
                getWisdom(),
                getCharisma()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardStatsGlobal){
            CardStatsGlobal other = (CardStatsGlobal) obj;
            return cardId == other.cardId &&
                    amountCardsPrinted == other.amountCardsPrinted &&
                    amountCardsBurned == other.amountCardsBurned &&
                    cardFightsTotal == other.cardFightsTotal &&
                    charisma == other.charisma;
        }else{
            return false;
        }
    }

    @Override
    public int compareTo(@NotNull CardStatsGlobal o) {
        return Integer.compare(cardFightsTotal, o.cardFightsTotal);
    }

    public void incrementCardPrint() {
        amountCardsPrinted++;
    }
}
