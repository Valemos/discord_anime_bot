package bot.commands.handlers.user;

import bot.MessageSenderMock;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CharacterCardGlobal;
import game.cards.CharacterCardPersonal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DropHandlerTest {

    AnimeCardsGame game;
    private CharacterCardGlobal card1;
    private MessageSenderMock senderMock;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();

        senderMock = new MessageSenderMock(game, List.of(new DropHandler()));

        card1 = new CharacterCardGlobal("1", "2", "3");
    }

    @Test
    void testPersonalCardCreatedFromGlobalCard() {
        CharacterCardPersonal card = card1.getPersonalCardForPickDelay(senderMock.player.getId(), 0);
        game.addCard(card1);

        assertEquals(card.getStats(), card1.getStats().getStatsForPickDelay(0));
        assertNotSame(card.getStats(), card1.getStats().getStatsForPickDelay(0));

        CharacterCardPersonal copiedCard = game.pickPersonalCardWithDelay(senderMock.player, card1.getCardId(), 0);
        assertEquals(card, copiedCard);
        assertNotSame(card, copiedCard);
    }
}