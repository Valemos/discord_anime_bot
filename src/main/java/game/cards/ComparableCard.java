package game.cards;

public interface ComparableCard {

    CardStats getStats();
    float getApprovalRating();
    int getPrint();

    static int comparatorFavor(ComparableCard c1, ComparableCard c2){
        return Float.compare(c2.getApprovalRating(), c1.getApprovalRating());
    }

    static int comparatorPower(ComparableCard c1, ComparableCard c2){
        return Float.compare(c1.getStats().getPowerLevel(), c2.getStats().getPowerLevel());
    }
}
