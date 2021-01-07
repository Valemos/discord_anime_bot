package bot.commands.handlers;

import bot.MessageSenderMock;
import bot.commands.handlers.creator.CreateGlobalCardHandler;
import bot.commands.handlers.user.DropHandler;
import game.AnimeCardsGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BotCommandHandlerTest {

    CreateGlobalCardHandler handlerCreate;
    DropHandler handlerDrop;
    AnimeCardsGame game;
    MessageSenderMock senderMock;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();
        handlerCreate = new CreateGlobalCardHandler();
        handlerDrop = new DropHandler();

        senderMock = new MessageSenderMock(game, List.of(handlerCreate, handlerDrop));
    }

    @Test
    void testArgumentsNotParsed_ifStringIsNotACommand() {
        senderMock.sendMessage("# command hello 123", senderMock.player.getId());
        senderMock.assertNoCommandHandled();
        senderMock.sendMessage(" hello 123", senderMock.player.getId());
        senderMock.assertNoCommandHandled();
        senderMock.sendMessage("hello 123", senderMock.player.getId());
        senderMock.assertNoCommandHandled();
    }

    @Test
    void testIncorrectCommandHandler() {
        assertThrows(RuntimeException.class, () -> handlerCreate.getArguments("#drop hello 123"));
        assertThrows(RuntimeException.class, () -> handlerCreate.getArguments("#drop"));
    }

    @Test
    void testArgumentsParsedCorrect() {
        assertLinesMatch(List.of("hello", "123"),
                handlerDrop.getArguments("#drop hello 123").commandParts);
    }
}