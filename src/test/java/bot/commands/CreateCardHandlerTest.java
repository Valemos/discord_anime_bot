package bot.commands;

import game.AnimeCardsGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CreateCardHandlerTest {
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
    void testArgumentsParsedCorrect() {
        assertArrayEquals(new String[]{"hello", "123"},
                handler.getArguments("#createcard hello 123"));
    }
}