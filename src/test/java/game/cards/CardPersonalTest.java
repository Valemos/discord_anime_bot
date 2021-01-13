package game.cards;

import game.Charisma;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardPersonalTest {

    @Test
    void testCardPowerLevel() {
        CharacterInfo info = new CharacterInfo("name", "series", "url");
        CardPersonal card = new CardPersonal(info,
                new CardStatsConstant(
                        10,
                        10,
                        10,
                        10,
                        Charisma.NEUTRAL));
        assertEquals(40, card.getPowerLevel());
    }
}