package game.cards;

public interface ComparableCard {

    CardStats getStats();
    float getApprovalRating();
    int getPrint();

    static int comparatorFavor(ComparableCard c1, ComparableCard c2){
        return Float.compare(c2.getApprovalRating(), c1.getApprovalRating());
    }

    static int comparatorPower(ComparableCard c1, ComparableCard c2){
        return Float.compare(c2.getStats().getPowerLevel(), c1.getStats().getPowerLevel());
    }

    static int comparatorPrint(ComparableCard c1, ComparableCard c2) {
        return Integer.compare(c2.getPrint(), c1.getPrint());
    }
}
