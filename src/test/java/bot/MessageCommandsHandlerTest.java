package bot;

import bot.commands.CommandInfo;
import bot.commands.CommandParser;
import bot.commands.handlers.BotCommandHandler;
import bot.commands.handlers.creator.CreateGlobalCardHandler;
import bot.commands.handlers.user.DropHandler;
import bot.commands.handlers.user.ShowCollectionHandler;
import game.AnimeCardsGame;
import game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageCommandsHandlerTest {
    private MessageCommandsHandler commandHandler;
    private AnimeCardsGame game;
    Player user;
    Player admin;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();
        commandHandler = new MessageCommandsHandler(game);
        commandHandler.setCommands(
                new ArrayList<>(List.of(
                        new DropHandler(),
                        new CreateGlobalCardHandler(),
                        new ShowCollectionHandler()
                )));

        user = new Player("1", AccessLevel.USER);
        admin = new Player("2", AccessLevel.ADMIN);

    }

    @Test
    void testCommandNameExtracted() {
        assertEquals("", CommandParser.getCommandName(""));
        assertEquals("", CommandParser.getCommandName("drop"));
        assertEquals("", CommandParser.getCommandName("drop test"));
        assertEquals("", CommandParser.getCommandName("# drop test"));
        assertEquals("drop", CommandParser.getCommandName("#drop"));
        assertEquals("drop", CommandParser.getCommandName("#drop test test"));
    }

    @Test
    void testIncorrectCommand() {
        assertNull(commandHandler.findCommandForString("#asdfds"));
    }

    @Test
    void testCommandFound(){
        String name = (new ShowCollectionHandler()).getCommandInfo().name;
        BotCommandHandler command = commandHandler.findCommandForString(CommandInfo.commandChar + name);
        assertEquals(command.getClass(), ShowCollectionHandler.class);
    }

    @Test
    void testCommandAliasFound() {
        String name = (new ShowCollectionHandler()).getCommandInfo().alias;
        BotCommandHandler command = commandHandler.findCommandForString(CommandInfo.commandChar + name);
        assertEquals(command.getClass(), ShowCollectionHandler.class);
    }

    @Test
    void testUserHasAccessToCommand() {
        game.addPlayer(admin);

        assertTrue(commandHandler.playerHasAccessToCommand(user, new DropHandler()));
        assertTrue(commandHandler.playerHasAccessToCommand(admin, new DropHandler()));
        assertFalse(commandHandler.playerHasAccessToCommand(admin, new CreateGlobalCardHandler()));
    }

    @Test
    void testGetExistingPlayer() {
        game.addPlayer(user);

        Player fetchedPlayer = commandHandler.getExistingOrCreateNewPlayerById(user.getId());
        assertSame(user, fetchedPlayer);
    }

    @Test
    void testNewPlayerCreatedWhenNotFound() {
        game.addPlayer(admin);

        Player newPlayer = commandHandler.getExistingOrCreateNewPlayerById("15");
        assertEquals("15", newPlayer.getId());
        assertNotSame(admin, newPlayer);
    }
}