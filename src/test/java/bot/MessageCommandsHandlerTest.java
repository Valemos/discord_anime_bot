package bot;

import bot.commands.BotCommandHandler;
import bot.commands.CreateCardHandler;
import bot.commands.DropHandler;
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
                        new CreateCardHandler()
                )));

        user = new Player("1", AccessLevel.USER);
        admin = new Player("2", AccessLevel.ADMIN);

    }

    @Test
    void testCommandNameExtracted() {
        assertEquals("", BotCommandHandler.getCommandName(""));
        assertEquals("", BotCommandHandler.getCommandName("drop"));
        assertEquals("", BotCommandHandler.getCommandName("drop test"));
        assertEquals("", BotCommandHandler.getCommandName("# drop test"));
        assertEquals("drop", BotCommandHandler.getCommandName("#drop"));
        assertEquals("drop", BotCommandHandler.getCommandName("#drop test test"));
    }

    @Test
    void testIncorrectCommand() {
        assertNull(commandHandler.findCommandForString("#asdfds"));
    }

    @Test
    void testCommandHandled(){
        assertEquals(commandHandler.findCommandForString("#drop").getClass(), DropHandler.class);
    }

    @Test
    void testUserHasAccessToCommand() {
        game.addPlayer(admin);

        assertTrue(commandHandler.playerHasAccessToCommand(user, new DropHandler()));
        assertTrue(commandHandler.playerHasAccessToCommand(admin, new DropHandler()));
        assertFalse(commandHandler.playerHasAccessToCommand(admin, new CreateCardHandler()));
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