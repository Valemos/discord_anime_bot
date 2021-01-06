package bot.commands.handlers;

import bot.commands.handlers.creator.CreateCardHandler;
import game.AnimeCardsGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotCommandHandlerTest {

    CreateCardHandler handler;
    AnimeCardsGame game;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();
        handler = new CreateCardHandler();
    }

    @Test
    void testArgumentsNotParsed_ifStringIsNotACommand() {
        assertNull(handler.getArguments("# command hello 123"));
        assertNull(handler.getArguments(" hello 123"));
        assertNull(handler.getArguments("hello 123"));
    }

    @Test
    void testIncorrectCommandHandler() {
        assertNull(handler.getArguments("#drop hello 123"));
        assertNull(handler.getArguments("#drop"));
    }

    @Test
    void testArgumentsParsedCorrect() {
        assertArrayEquals(new String[]{"hello", "123"},
                handler.getArguments("#createcard hello 123").commandParts);
    }
}