package game.cards;

public class CardPickInfo {
    float pickDelay;
    int cardFights;

    public CardPickInfo(float pickDelay, int cardFights) {
        this.pickDelay = pickDelay;
        this.cardFights = cardFights;
    }

    public float getPickDelay() {
        return pickDelay;
    }

    public void setPickDelay(float pickDelay) {
        this.pickDelay = pickDelay;
    }

    public int getCardFights() {
        return cardFights;
    }

    public void setCardFights(int cardFights) {
        this.cardFights = cardFights;
    }
}
