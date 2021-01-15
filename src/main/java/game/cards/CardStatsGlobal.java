package game.cards;

import game.Charisma;
import org.jetbrains.annotations.NotNull;

public class CardStatsGlobal implements Comparable<CardStatsGlobal> {

    int cardId;
    int amountCardsPrinted;
    int amountCardsDropped;
    int cardFightsTotal;
    Charisma charisma;


    public CardStatsGlobal() {
        this(-1, 0, 0, 0, Charisma.fromValue(0));
    }

    public CardStatsGlobal(int cardId, int amountCardsPrinted, int amountCardsDropped, int cardFightsTotal, Charisma charisma) {
        this.cardId = cardId;
        this.amountCardsPrinted = amountCardsPrinted;
        this.amountCardsDropped = amountCardsDropped;
        this.cardFightsTotal = cardFightsTotal;
        this.charisma = charisma;
    }

    public float getApprovalRating(){
        if (amountCardsDropped != 0){
            return (amountCardsPrinted - amountCardsDropped) / (float)amountCardsDropped;
        }else{
            return amountCardsPrinted - amountCardsDropped;
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
                dexterityFunction(delayCardPicked),
                getStrength(),
                getWisdom(),
                getCharisma()
        );
    }

    private float dexterityFunction(float delayCardPicked) {
        return delayCardPicked;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardStatsGlobal){
            CardStatsGlobal other = (CardStatsGlobal) obj;
            return cardId == other.cardId &&
                    amountCardsPrinted == other.amountCardsPrinted &&
                    amountCardsDropped == other.amountCardsDropped &&
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
}
