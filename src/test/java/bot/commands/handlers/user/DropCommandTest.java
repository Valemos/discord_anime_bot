package bot.commands.handlers.user;

import bot.MessageSenderMock;
import game.AnimeCardsGame;
import game.cards.CharacterCardGlobal;
import game.cards.CharacterCardPersonal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DropCommandTest {

    AnimeCardsGame game;
    private CharacterCardGlobal card1;
    private MessageSenderMock sender;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();

        sender = new MessageSenderMock(game, List.of(new DropCommand()));

        card1 = new CharacterCardGlobal("1", "2", "3");
    }

    @Test
    void testPersonalCardCreatedFromGlobalCard() {
        CharacterCardPersonal card = card1.getPersonalCardForPickDelay(sender.player.getId(), 0);
        game.addCard(card1);

        assertEquals(card.getStats(), card1.getStats().getStatsForPickDelay(0));
        assertNotSame(card.getStats(), card1.getStats().getStatsForPickDelay(0));

        CharacterCardPersonal copiedCard = game.pickPersonalCardWithDelay(sender.player, card1.getCardId(), 0);
        assertEquals(card, copiedCard);
        assertNotSame(card, copiedCard);
    }
}