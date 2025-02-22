package bot;

import bot.commands.user.DailyCommand;
import bot.commands.user.shop.MessageSenderTester;
import game.Player;
import game.materials.Material;
import game.materials.MaterialsSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotAnimeCardsTest extends MessageSenderTester {

    @Test
    void testSendNotACommand() {
        send("not a command");
        sender.assertCommandNotHandled();
    }

    @Test
    void testDailyCommandHandled() {
        send("#daily");
        sender.assertCommandHandled(DailyCommand.class);
    }

    @Test
    void testDailyCommandGold() {
        MaterialsSet materials = tester().getMaterials();
        int startGold = materials.getAmount(Material.GOLD);

        send("#daily");
        sender.assertCommandHandled(DailyCommand.class);

        materials = tester().getMaterials();
        int goldDifference = materials.getAmount(Material.GOLD) - startGold;
        assertTrue(goldDifference >= 50 && goldDifference <= 200);
    }
}