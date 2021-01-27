package bot.commands.owner;

import bot.commands.AbstractCommand;
import bot.commands.AbstractCommandTest;
import game.Player;
import game.cards.CardGlobal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddCardCommandTest extends AbstractCommandTest<AddCardCommand, AddCardCommand.Arguments> {

    @BeforeEach
    void setUp() {
        setCommand(new AddCardCommand(spyGame));
    }

    @Test
    void testCreateCardCommand() {
        assertNull(spyGame.getCardGlobalUnique("card name", "card series"));

        AddCardCommand.Arguments args = createArguments();
        args.name = "card name";
        args.series = "card series";
        args.imageUrl = "image_url";

        handleCommand(tester, args);

        CardGlobal c = spyGame.getCardGlobalUnique("card name", "card series");
        assertNotNull(c);
        assertEquals("card name", c.getCharacterInfo().getName());
        assertEquals("card series", c.getCharacterInfo().getSeriesName());
        assertEquals("image_url", c.getCharacterInfo().getImageUrl());
    }
}