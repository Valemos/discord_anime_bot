package game.cards;

import game.CharismaState;

public class CardStatsConstant extends CardStats{
    public CardStatsConstant(float dexterity, float intelligence, float defenseLevel, CharismaState charisma) {
        super(dexterity, intelligence, defenseLevel, charisma);
    }

    public static CardStatsConstant fromUpdatable(CardStatsUpdatable statsUpdatable){
        return new CardStatsConstant(
                statsUpdatable.dexterity,
                statsUpdatable.intelligence,
                statsUpdatable.defenseLevel,
                statsUpdatable.charisma);
    }
}
