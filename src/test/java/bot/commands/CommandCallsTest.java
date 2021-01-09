package bot.commands;

import bot.MessageSenderMock;
import bot.commands.handlers.BotCommandHandler;
import bot.commands.handlers.creator.CreateGlobalCardHandler;
import bot.commands.handlers.creator.JoinAsCreatorHandler;
import bot.commands.handlers.creator.JoinAsTesterHandler;
import bot.commands.handlers.user.DropHandler;
import bot.commands.handlers.user.InspectCardHandler;
import bot.commands.handlers.user.ShowCollectionHandler;
import game.AnimeCardsGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class CommandCallsTest {

    private AnimeCardsGame game;
    private MessageSenderMock sender;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();

        List<BotCommandHandler> allCommands = List.of(
                new JoinAsTesterHandler(),
                new DropHandler(),
                new CreateGlobalCardHandler(),
                new JoinAsCreatorHandler(),
                new ShowCollectionHandler(),
                new InspectCardHandler()
        );

        sender = new MessageSenderMock(game, allCommands);

        sender.setDefaultPlayer(sender.creator);
    }

    @Test
    void testTester() {
        sender.assertAnyCommandHandledOnMessage("#test");
    }

    @Test
    void testDrop() {
        sender.assertAnyCommandHandledOnMessage("#drop");
    }

    @Test
    void testCreator() {
        sender.assertAnyCommandHandledOnMessage("#makecreator");
    }

    @Test
    void testCreateCardAlias() {
        sender.assertCommandHandledOnMessage("#cr 0 0 0", CreateGlobalCardHandler.class);
    }

    @Test
    void testCreateCard() {
        sender.assertCommandHandledOnMessage("#createcard 0 0 0", CreateGlobalCardHandler.class);
    }

    @Test
    void testShowCollectionAlias() {
        sender.assertAnyCommandHandledOnMessage("#c");
    }

    @Test
    void testShowCollection() {
        sender.assertAnyCommandHandledOnMessage("#collection");
    }

    @Test
    void testShowCollectionForUser() {
        sender.assertAnyCommandHandledOnMessage("#collection 1");
    }

    @Test
    void testInspectCard() {
        sender.assertAnyCommandHandledOnMessage("#i 1");
    }
}