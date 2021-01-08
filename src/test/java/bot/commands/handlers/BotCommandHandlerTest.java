package bot.commands.handlers;

import bot.MessageSenderMock;
import bot.commands.handlers.creator.CreateGlobalCardHandler;
import bot.commands.handlers.user.DropHandler;
import game.AnimeCardsGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

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
    void testSingleWordArgumentsParsedCorrect() {
        DefaultMessageArguments args = (DefaultMessageArguments) handlerDrop.getArguments("#drop hello 123");
        assertLinesMatch(List.of("hello", "123"), args.commandParts);
    }

    @Test
    void testMultiWordArgumentsParsedCorrect() {
        DefaultMessageArguments args = (DefaultMessageArguments) handlerDrop.getArguments(
                "#drop \"hello test\" 123");
        assertLinesMatch(List.of("hello test", "123"), args.commandParts);

        args = (DefaultMessageArguments) handlerDrop.getArguments(
                "#drop \"hello test\" \"123 test\"");
        assertLinesMatch(List.of("hello test", "123 test"), args.commandParts);
    }

    @Test
    void testEmptyArgumentsCorrect() {
        EmptyMessageArguments args = (EmptyMessageArguments) handlerDrop.getArguments(
                "#drop \"hello test\" 123"
        );
        assertTrue(args.isValid());

        args = (EmptyMessageArguments) handlerDrop.getArguments(
                "#drop"
        );
        assertTrue(args.isValid());

        args = (EmptyMessageArguments) handlerDrop.getArguments(
                "#drop hello"
        );
        assertTrue(args.isValid());
    }

    @Test
    void testLeadingAndTrailingSpacesRemoved() {
        senderMock.sendMessage("    #drop command    ", senderMock.player.getId());
        verify(senderMock.getCommandSpy(DropHandler.class)).handleCommand(any());
    }
}