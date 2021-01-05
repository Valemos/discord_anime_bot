package game;

public class CardStats implements Cloneable{
    float dexterity;            // how quickly the card was picked up
    float intelligence;         // What condition the card was dropped in
    float defenseLevel;         // The level of armor the card has on
    CharismaState charisma;     // the card has a dye or frame or not
    Constitution constitution;  // the card is injured or not

    public CardStats() {
        this(0, 10, 0, CharismaState.NEUTRAL, Constitution.HEALTHY);
    }

    public CardStats(float dexterity, float intelligence, float defenseLevel, CharismaState charisma, Constitution constitution) {
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.defenseLevel = defenseLevel;
        this.charisma = charisma;
        this.constitution = constitution;
    }

    @Override
    public CardStats clone(){
        try{
            return (CardStats) super.clone();
        }catch (CloneNotSupportedException e){
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardStats){
            CardStats other = (CardStats) obj;
            return dexterity == other.dexterity &&
                    intelligence == other.intelligence &&
                    defenseLevel == other.defenseLevel &&
                    charisma == other.charisma &&
                    constitution == other.constitution;
        }else{
            return false;
        }
    }

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

    public CharismaState getCharisma() {
        return charisma;
    }

    public Constitution getConstitution() {
        return constitution;
    }
}
