package game.cards;

import game.CharismaState;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CardStatsUpdatable extends CardStats {

    public CardStatsUpdatable() {
        super();
    }

    public CardStatsUpdatable(CardStats stats) {
        super(stats.dexterity, stats.intelligence, stats.defenseLevel, stats.charisma);
    }

    public float getApprovalRating(){
        // TODO
        // amount of the card in circulation
        // divided by the amount of the card was dropped in total
        return 100;
    }

    public float getDexterity() {
        return dexterity;
    }

    public float getStrength() {
        // TODO calculate strength for all cards of this type
        return 100;
    }

    public float getWisdom() {
        // TODO
        // Value increases as the print? of the card
        // decreases compared to the amount of that card has generated
        // "rarity" of the card?
        return 100;
    }

    public float getIntelligence() {
        return intelligence;
    }

    public CharismaState getCharisma() {
        return charisma;
    }

    public CardStatsConstant getConstantCopy() {
        return CardStatsConstant.fromUpdatable(this);
    }
}
