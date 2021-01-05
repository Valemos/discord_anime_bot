package game;

public class CardStats {
    float dexterity;
    float intelligence;
    float defenseLevel;
    float attackPower;
    CharismaState charisma;
    Constitution constitution;

    public float getApprovalRating(){
        // TODO
        // amount of the card in circulation
        // divided by the amount of the card was dropped in total
        return 100;
    }

    public float getWisdom() {
        // TODO
        // Value increases as the print? of the card
        // decreases compared to the amount of that card has generated
        // "rarity" of the card?
        return 100;
    }

    public float getStrength() {
        // TODO calculate strength for all cards of this type
        return 100;
    }

    public float getDexterity() {
        return dexterity;
    }

    public float getIntelligence() {
        return intelligence;
    }

    public float getDefenseLevel() {
        return defenseLevel;
    }

    public float getAttackPower() {
        return attackPower;
    }

    public CharismaState getCharisma() {
        return charisma;
    }

    public Constitution getConstitution() {
        return constitution;
    }
}
