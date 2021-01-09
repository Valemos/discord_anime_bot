package bot;

import bot.commands.CommandInfo;
import bot.commands.handlers.BotCommandHandler;
import bot.commands.handlers.creator.CreateGlobalCardHandler;
import bot.commands.handlers.user.DropHandler;
import bot.commands.handlers.user.ShowCollectionHandler;
import game.AnimeCardsGame;
import game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageCommandsHandlerTest {
    private AnimeCardsGame game;
    private MessageSenderMock senderMock;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();
        senderMock = new MessageSenderMock(game, List.of(
                new DropHandler(),
                new CreateGlobalCardHandler(),
                new ShowCollectionHandler()
        ));
    }

    @Test
    void testCommandsHandling() {
        senderMock.assertNoCommandHandledOnMessage("");
        senderMock.assertNoCommandHandledOnMessage("#");
        senderMock.assertNoCommandHandledOnMessage("cr");
        senderMock.assertNoCommandHandledOnMessage("createcard");
        senderMock.assertNoCommandHandledOnMessage("createcard test");
        senderMock.assertNoCommandHandledOnMessage("# drop test");
        senderMock.assertCommandHandledOnMessage("#drop", DropHandler.class);
    }

    @Test
    void testMoreArgumentsThanNeeded_forCommandWithNoArguments() {
        senderMock.assertCommandHandledOnMessage("#drop test test", DropHandler.class);
    }

    @Test
    void testIncorrectCommand() {
        senderMock.assertNoCommandHandledOnMessage("#asdfds");
        assertNull(senderMock.handler.findCommandForString("#asdfds"));
    }

    @Test
    void testCommandAliasFound() {
        String name = (new ShowCollectionHandler()).getCommandInfo().alias;
        BotCommandHandler command = senderMock.handler.findCommandForString(CommandInfo.commandChar + name);
        assertSame(command, senderMock.getCommandSpy(ShowCollectionHandler.class));
    }

    @Test
    void testUserHasAccessToCommand() {
        assertTrue(senderMock.handler.playerHasAccessToCommand(senderMock.player, new DropHandler()));
        assertTrue(senderMock.handler.playerHasAccessToCommand(senderMock.admin, new DropHandler()));
        assertFalse(senderMock.handler.playerHasAccessToCommand(senderMock.admin, new CreateGlobalCardHandler()));
        assertTrue(senderMock.handler.playerHasAccessToCommand(senderMock.creator, new CreateGlobalCardHandler()));
    }

    @Test
    void testGetExistingPlayer() {
        Player fetchedPlayer = senderMock.handler.getExistingOrCreateNewPlayerById(senderMock.player.getId());
        assertSame(senderMock.player, fetchedPlayer);
    }

    @Test
    void testNewPlayerCreatedWhenNotFound() {
        Player newPlayer = senderMock.handler.getExistingOrCreateNewPlayerById("15");
        assertEquals("15", newPlayer.getId());
        assertEquals(AccessLevel.USER, newPlayer.getAccessLevel());
        assertNotSame(senderMock.admin, newPlayer);
    }

    @Test
    void testDifferentArguments() {
        senderMock.sendMessage("#createcard ");
        senderMock.assertCommandNotHandled(CreateGlobalCardHandler.class);
    }
}