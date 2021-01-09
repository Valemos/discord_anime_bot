package bot.commands.handlers.creator;

import bot.MessageSenderMock;
import bot.commands.arguments.MessageArguments;
import game.AnimeCardsGame;
import game.cards.CardStatsGlobal;
import game.cards.CharacterCardGlobal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CreateGlobalCardHandlerTest {

    AnimeCardsGame game;
    CreateGlobalCardHandler command;
    String commandName;

    MessageSenderMock sender;


    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();

        command = new CreateGlobalCardHandler();
        commandName = "#" + command.getCommandInfo().name;

        sender = new MessageSenderMock(game, List.of(command));
        sender.setDefaultPlayer(sender.creator);
    }

    @Test
    void testParametersValid() {
        sender.assertCommandHandledOnMessage(commandName + " hello i am", command.getClass());
    }

    @Test
    void testParametersNumberInsufficient() {
        sender.assertCommandNotHandledOnMessage(commandName + " hello", command.getClass());
    }

    @Test
    void testTwoOptionalParameters() {
        sender.assertCommandHandledOnMessage(commandName + " hello i am test", command.getClass());
    }

    @Test
    void testArgumentsWithBrackets() {
        sender.assertCommandHandledOnMessage(
                commandName + " \"two words\" \"three words here\" //url",
                command.getClass());

        MessageArguments args = sender.captureLastCommandParameters(command.getClass()).messageArgs;

        assertEquals("two words", args.get(0));
        assertEquals("three words here", args.get(1));
        assertEquals("//url", args.get(2));
        assertNull(args.get(3));
    }

    @Test
    void testIncorrectBrackets() {
        sender.sendMessage(commandName + " \"two words three words here //url");
        sender.assertCommandNotHandled(command.getClass());
    }

    @Test
    void testPlayerCannotCreateCards() {
        sender.sendMessage(commandName + " test test url", sender.player.getId());
        verify(sender.spyGame, never()).addCard(any());
    }

    @Test
    void testAdminCannotCreateCards() {
        sender.sendMessage(commandName + " test test url", sender.admin.getId());
        verify(sender.spyGame, never()).addCard(any());
    }

    @Test
    void testGlobalCardCreated() {

        sender.sendMessage(commandName + " \"two words\" \"three words here\" //url");
        verify(sender.spyGame).addCard(sender.cardArgument.capture());

        CharacterCardGlobal card = sender.cardArgument.getValue();
        assertEquals("two words", card.getCharacterInfo().getCharacterName());
        assertEquals("three words here", card.getCharacterInfo().getSeriesName());
        assertEquals("//url", card.getCharacterInfo().getImageUrl());
        assertEquals(new CardStatsGlobal(), card.getStats());
    }
}