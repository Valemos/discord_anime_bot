package bot.commands;

import bot.MessageSenderMock;
import bot.commands.handlers.AbstractBotCommand;
import bot.commands.handlers.creator.CreateGlobalCardCommand;
import bot.commands.handlers.creator.JoinAsCreatorCommand;
import bot.commands.handlers.creator.JoinAsTesterCommand;
import bot.commands.handlers.user.DropCommand;
import bot.commands.handlers.user.InspectCardCommand;
import bot.commands.handlers.user.ShowCollectionCommand;
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

        List<AbstractBotCommand> allCommands = List.of(
                new JoinAsTesterCommand(),
                new DropCommand(),
                new CreateGlobalCardCommand(),
                new JoinAsCreatorCommand(),
                new ShowCollectionCommand(),
                new InspectCardCommand()
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
        sender.assertCommandHandledOnMessage("#cr 0 0 0", CreateGlobalCardCommand.class);
    }

    @Test
    void testCreateCard() {
        sender.assertCommandHandledOnMessage("#createcard 0 0 0", CreateGlobalCardCommand.class);
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
    void testShowCollectionWithOptionalArgument() {
        sender.assertAnyCommandHandledOnMessage("#collection 1 s=name");
    }

    @Test
    void testInspectCard() {
        sender.assertAnyCommandHandledOnMessage("#i 1");
    }
}