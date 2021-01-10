package bot;

import bot.commands.CommandInfo;
import bot.commands.parsing.ArgumentParser;
import bot.commands.parsing.MessageArguments;
import bot.commands.handlers.AbstractBotCommand;
import bot.commands.handlers.creator.CreateGlobalCardCommand;
import bot.commands.handlers.user.DropCommand;
import bot.commands.handlers.user.ShowCollectionCommand;
import game.AnimeCardsGame;
import game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

class MessageCommandsHandlerTest {
    private AnimeCardsGame game;
    private MessageSenderMock sender;
    private CreateGlobalCardCommand handlerCreate;
    private String commandName;
    private DropCommand handlerDrop;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();
        handlerCreate = new CreateGlobalCardCommand();
        handlerDrop = new DropCommand();
        commandName = CommandInfo.commandChar + handlerCreate.getCommandInfo().name;
        sender = new MessageSenderMock(game, List.of(
                handlerCreate,
                handlerDrop,
                new ShowCollectionCommand()
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
        sender.assertCommandHandledOnMessage("#drop", DropCommand.class);
    }

    @Test
    void testMoreArgumentsThanNeeded_forCommandWithNoArguments() {
        sender.assertCommandHandledOnMessage("#drop test test", DropCommand.class);
    }

    @Test
    void testIncorrectCommand() {
        sender.assertNoCommandHandledOnMessage("#asdfds");
        assertNull(sender.handler.findCommandForString("#asdfds"));
    }

    @Test
    void testCommandAliasFound() {
        String name = (new ShowCollectionCommand()).getCommandInfo().alias;
        AbstractBotCommand command = sender.handler.findCommandForString(CommandInfo.commandChar + name);
        assertSame(command, sender.getCommandSpy(ShowCollectionCommand.class));
    }

    @Test
    void testUserHasAccessToCommand() {
        assertTrue(sender.handler.playerHasAccessToCommand(sender.player, new DropCommand()));
        assertTrue(sender.handler.playerHasAccessToCommand(sender.admin, new DropCommand()));
        assertFalse(sender.handler.playerHasAccessToCommand(sender.admin, new CreateGlobalCardCommand()));
        assertTrue(sender.handler.playerHasAccessToCommand(sender.creator, new CreateGlobalCardCommand()));
    }

    @Test
    void testGetExistingPlayer() {
        Player fetchedPlayer = sender.handler.getPlayerById(sender.player.getId());
        assertSame(sender.player, fetchedPlayer);
    }

    @Test
    void testNewPlayerCreatedWhenNotFound() {
        Player newPlayer = sender.handler.getPlayerById("15");
        assertEquals("15", newPlayer.getId());
        assertEquals(PlayerAccessLevel.USER, newPlayer.getAccessLevel());
        assertNotSame(sender.admin, newPlayer);
    }

    @Test
    void testNoArguments() {
        sender.sendMessage("#createcard ");
        sender.assertCommandNotHandled(CreateGlobalCardCommand.class);
    }

    @Test
    void testArgumentsNotParsed_ifStringIsNotACommand() {
        sender.sendMessage("# command hello 123", sender.player.getId());
        sender.assertNoCommandHandled();
        sender.sendMessage(" hello 123", sender.player.getId());
        sender.assertNoCommandHandled();
        sender.sendMessage("hello 123", sender.player.getId());
        sender.assertNoCommandHandled();
    }

    @Test
    void testArgumentsWithBrackets() {
        sender.assertCommandHandledOnMessage(
                commandName + " \"two words\" \"three words here\" //url",
                handlerCreate.getClass());

        MessageArguments args = sender.getLastCommandParameters(handlerCreate.getClass()).messageArgs;

        assertEquals("two words", args.get(0));
        assertEquals("three words here", args.get(1));
        assertEquals("//url", args.get(2));
        assertNull(args.get(3));
    }

    @Test
    void testIncorrectBrackets() {
        sender.sendMessage(commandName + " \"two words three words here //url");
        sender.assertCommandNotHandled(handlerCreate.getClass());
    }

    @Test
    void testSingleWordArgumentsParsedCorrect() {
        MessageArguments args = handlerCreate.parseArguments(
                "#"+handlerCreate.getCommandInfo().name+" hello 123"
        );
        assertLinesMatch(List.of("hello", "123"), args.getValues());
    }

    @Test
    void testMultiWordArgumentsParsedCorrect() {
        MessageArguments args = handlerCreate.parseArguments(
                "#"+handlerCreate.getCommandInfo().name+" \"hello test\" 123");
        assertLinesMatch(List.of("hello test", "123"), args.getValues());

        args = handlerCreate.parseArguments(
                "#"+handlerCreate.getCommandInfo().name+" \"hello test\" \"123 test\"");
        assertLinesMatch(List.of("hello test", "123 test"), args.getValues());
    }

    @Test
    void testEmptyArgumentsCorrect() {
        MessageArguments args = handlerDrop.parseArguments(
                "#drop \"hello test\" 123"
        );
        assertTrue(args.isValid());

        args = handlerDrop.parseArguments(
                "#drop"
        );
        assertTrue(args.isValid());

        args = handlerDrop.parseArguments(
                "#drop hello"
        );
        assertTrue(args.isValid());
    }

    @Test
    void testLeadingAndTrailingSpacesRemoved() {
        sender.assertCommandHandledOnMessage(
                "    #drop command    ",
                sender.player.getId(),
                DropCommand.class);
    }

    @Test
    void testSpacesRegex() {
        Pattern pattern = ArgumentParser.repeatedSpacesRegex;

        assertEquals(pattern.matcher("     ").replaceAll(" "), " ");
        assertEquals(pattern.matcher("\t\t").replaceAll(" "), " ");
        assertEquals(pattern.matcher("  \t \t  \t").replaceAll(" "), " ");
    }

    @Test
    void testMultipleSpacesBetweenArguments() {
        sender.assertCommandHandledOnMessage(
                "    #cr command   \"new card\"\t\t \"test\" ",
                CreateGlobalCardCommand.class);
        MessageArguments args = sender.getLastCommandParameters(CreateGlobalCardCommand.class).messageArgs;
        assertEquals("command", args.get(0));
        assertEquals("new card", args.get(1));
        assertEquals("test", args.get(2));
        assertNull(args.get(3));
    }

    @Test
    void testSpacesInsideQuotes() {
        sender.assertCommandHandledOnMessage(
                "#cr \"test \" \" new card \" ",
                CreateGlobalCardCommand.class);
        MessageArguments args = sender.getLastCommandParameters(CreateGlobalCardCommand.class).messageArgs;
        assertEquals("test ", args.get(0));
        assertEquals(" new card ", args.get(1));
    }
}