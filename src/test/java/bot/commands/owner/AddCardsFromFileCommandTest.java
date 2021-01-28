package bot.commands.owner;

import bot.commands.AbstractCommandNoArguments;
import bot.commands.AbstractCommandTest;
import game.cards.CardGlobal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

class AddCardsFromFileCommandTest extends AbstractCommandTest<AddCardsFromFileCommand, AbstractCommandNoArguments.EmptyArguments> {
    private final String testFileName = "test_cards.txt";
    AddCardCommand.Arguments addCardArgs;

    @BeforeEach
    void setUp() {
        setCommand(new AddCardsFromFileCommand(spyGame));
        AddCardCommand addCardCommand = new AddCardCommand(spyGame);
        addCardArgs = createArguments(addCardCommand);
    }

    @Test
    void testCardsWithErrorsNotAdded() {

        doNothing().when(spyGame).addCard(any());

        assertDoesNotThrow(() -> {
            AddCardsFromFileCommand.handleFileLine(spyGame, addCardArgs, "name series url");
            AddCardsFromFileCommand.handleFileLine(spyGame, addCardArgs, "name \"multi word series\" url");
            AddCardsFromFileCommand.handleFileLine(spyGame, addCardArgs, "\"multi word name\" series url");
        });

        assertThrows(CmdLineException.class, () -> {
            AddCardsFromFileCommand.handleFileLine(spyGame, addCardArgs, "series url");
            AddCardsFromFileCommand.handleFileLine(spyGame, addCardArgs, "name series url multi word");
            AddCardsFromFileCommand.handleFileLine(spyGame, addCardArgs, "name series multi \"word url\"");
        });
    }

    @Test
    void testNewCardAdded() throws CmdLineException {
        CardGlobal card = spyGame.getCardGlobalUnique("name", "series");
        assertNull(card);

        AddCardsFromFileCommand.handleFileLine(spyGame, addCardArgs,
                "name series url");
        AddCardsFromFileCommand.handleFileLine(spyGame, addCardArgs,
                "name series url");

        card = spyGame.getCardGlobalUnique("name", "series");
        assertNotNull(card);
    }
}