package game.cards;

import bot.commands.AbstractCommandTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardStatsGlobalTest {

    CardStatsGlobal stats;

    @BeforeEach
    void setUp() {
        stats = new CardStatsGlobal();
    }

    @Test
    void testWisdomFunctionReturnsBase() {
        float base = 50;
        float res = stats.getWisdom(base, 100);
        assertEquals(base, stats.getWisdom(base, 1));
        assertEquals(base, stats.getWisdom(base, 0));
        assertEquals(base, stats.getWisdom(base, -1000));
    }

    @Test
    void testHalfLifeValue() {
        float base = 50;
        assertEquals(base / 2, stats.getWisdom(base, CardStatsGlobal.wisdomHalfLife));
    }
}