package bot.commands;

import bot.MessageSenderMock;
import bot.commands.handlers.creator.CreateGlobalCardHandler;
import game.AnimeCardsGame;
import game.cards.CardStatsUpdatable;
import game.cards.CharacterCardGlobal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class CreateGlobalCardHandlerTest {

    AnimeCardsGame game;
    CreateGlobalCardHandler command;
    CreateCardArguments args;
    String commandName;

    MessageSenderMock senderMock;


    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();

        command = new CreateGlobalCardHandler();
        args = new CreateCardArguments(command.getCommandInfo());
        commandName = "#" + command.getCommandInfo().name;

        senderMock = new MessageSenderMock(game, List.of(command));
    }

    @Test
    void testParametersValid() {
        args.fromString(commandName + " hello i am");
        assertTrue(args.isValid());
    }

    @Test
    void testParametersNumberInsufficient() {
        args.fromString(commandName + " hello i");
        assertFalse(args.isValid());

        args.fromString(commandName);
        assertFalse(args.isValid());
    }

    @Test
    void testTooMuchParameters() {
        args.fromString(commandName + " hello i am test");
        assertFalse(args.isValid());
    }

    @Test
    void testArgumentsWithBrackets() {
        args.fromString(commandName + " \"two words\" \"three words here\" //url");

        assertTrue(args.isValid());

        assertEquals("two words", args.characterName);
        assertEquals("three words here", args.seriesName);
        assertEquals("//url", args.imageUrl);
    }

    @Test
    void testIncorrectBrackets() {
        args.fromString(commandName + " \"two words three words here //url");
        assertFalse(args.isValid());
    }

    @Test
    void testPlayerCannotCreateCards() {
        senderMock.sendMessage(commandName + " test test url", senderMock.player.getId());
        verify(senderMock.spyGame, never()).addCard(any());
    }

    @Test
    void testAdminCannotCreateCards() {
        senderMock.sendMessage(commandName + " test test url", senderMock.admin.getId());
        verify(senderMock.spyGame, never()).addCard(any());
    }

    @Test
    void testGlobalCardCreated() {

        senderMock.sendMessage(commandName + " \"two words\" \"three words here\" //url", senderMock.creator.getId());
        verify(senderMock.spyGame).addCard(senderMock.cardArgument.capture());

        CharacterCardGlobal card = senderMock.cardArgument.getValue();
        assertEquals("two words", card.getCharacterInfo().getCharacterName());
        assertEquals("three words here", card.getCharacterInfo().getSeriesName());
        assertEquals("//url", card.getCharacterInfo().getImageUrl());
        assertEquals(new CardStatsUpdatable(), card.getStats());
    }
}