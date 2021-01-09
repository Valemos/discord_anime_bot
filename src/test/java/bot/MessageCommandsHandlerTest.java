package bot;

import bot.commands.CommandInfo;
import bot.commands.arguments.MessageArguments;
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
    private MessageSenderMock sender;
    private CreateGlobalCardHandler command;
    private String commandName;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();
        command = new CreateGlobalCardHandler();
        commandName = CommandInfo.commandChar + command.getCommandInfo().name;
        sender = new MessageSenderMock(game, List.of(
                command,
                new DropHandler(),
                new ShowCollectionHandler()
        ));
        sender.setDefaultPlayer(sender.creator);
    }

    @Test
    void testCommandsHandling() {
        sender.assertNoCommandHandledOnMessage("");
        sender.assertNoCommandHandledOnMessage("#");
        sender.assertNoCommandHandledOnMessage("cr");
        sender.assertNoCommandHandledOnMessage("createcard");
        sender.assertNoCommandHandledOnMessage("createcard test");
        sender.assertNoCommandHandledOnMessage("# drop test");
        sender.assertCommandHandledOnMessage("#drop", DropHandler.class);
    }

    @Test
    void testMoreArgumentsThanNeeded_forCommandWithNoArguments() {
        sender.assertCommandHandledOnMessage("#drop test test", DropHandler.class);
    }

    @Test
    void testIncorrectCommand() {
        sender.assertNoCommandHandledOnMessage("#asdfds");
        assertNull(sender.handler.findCommandForString("#asdfds"));
    }

    @Test
    void testCommandAliasFound() {
        String name = (new ShowCollectionHandler()).getCommandInfo().alias;
        BotCommandHandler command = sender.handler.findCommandForString(CommandInfo.commandChar + name);
        assertSame(command, sender.getCommandSpy(ShowCollectionHandler.class));
    }

    @Test
    void testUserHasAccessToCommand() {
        assertTrue(sender.handler.playerHasAccessToCommand(sender.player, new DropHandler()));
        assertTrue(sender.handler.playerHasAccessToCommand(sender.admin, new DropHandler()));
        assertFalse(sender.handler.playerHasAccessToCommand(sender.admin, new CreateGlobalCardHandler()));
        assertTrue(sender.handler.playerHasAccessToCommand(sender.creator, new CreateGlobalCardHandler()));
    }

    @Test
    void testGetExistingPlayer() {
        Player fetchedPlayer = sender.handler.getExistingOrCreateNewPlayerById(sender.player.getId());
        assertSame(sender.player, fetchedPlayer);
    }

    @Test
    void testNewPlayerCreatedWhenNotFound() {
        Player newPlayer = sender.handler.getExistingOrCreateNewPlayerById("15");
        assertEquals("15", newPlayer.getId());
        assertEquals(AccessLevel.USER, newPlayer.getAccessLevel());
        assertNotSame(sender.admin, newPlayer);
    }

    @Test
    void testNoArguments() {
        sender.sendMessage("#createcard ");
        sender.assertCommandNotHandled(CreateGlobalCardHandler.class);
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

}