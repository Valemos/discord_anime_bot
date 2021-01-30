package game.cards;

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
                        10
                ));
        assertEquals(50, card.getPowerLevel());
    }
}