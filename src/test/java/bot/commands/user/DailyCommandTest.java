package bot.commands.user;

import bot.commands.user.shop.MessageSenderTester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DailyCommandTest extends MessageSenderTester {

    @Test
    void testGoldGivenInCertainRange() {
        DailyCommand daily = new DailyCommand(game());
        for (int i = 0; i < daily.rareProbability; i++) {
            int randomGold = daily.getRandomGold();
            assertTrue(randomGold >= daily.baseReward && randomGold <= daily.rareReward);
        }
    }
}