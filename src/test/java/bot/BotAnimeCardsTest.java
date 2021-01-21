package bot;

import bot.commands.user.DailyCommand;
import game.Player;
import game.materials.Material;
import game.materials.MaterialsSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotAnimeCardsTest {

    static BotMessageSenderMock sender;

    @BeforeAll
    static void setSender() throws Exception {
        sender = new BotMessageSenderMock();
    }

    @BeforeEach
    void setUp() {
        sender.reset();
    }

    @Test
    void testSendNotACommand() {
        sender.send("not a command");
        sender.assertCommandNotHandled();
    }

    @Test
    void testDailyCommandHandled() {
        sender.send("#daily");
        sender.assertCommandHandled(DailyCommand.class);
    }

    @Test
    void testDailyCommandGold() {
        Player tester = sender.tester1;

        MaterialsSet materials = tester.getMaterials();
        int startGold = materials.getAmount(Material.GOLD);

        sender.send("#daily");
        sender.assertCommandHandled(DailyCommand.class);

        materials = tester.getMaterials();
        int goldDifference = materials.getAmount(Material.GOLD) - startGold;
        assertTrue(goldDifference >= 50 && goldDifference <= 200);
    }
}