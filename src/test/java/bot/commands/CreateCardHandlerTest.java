package bot.commands;

import bot.AccessLevel;
import bot.commands.handlers.creator.CreateCardHandler;
import game.AnimeCardsGame;
import game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateCardHandlerTest {

    CreateCardHandler command;
    AnimeCardsGame game;
    Player player;

    @BeforeEach
    void setUp() {
        game = new AnimeCardsGame();

        player = new Player("1", AccessLevel.USER);
        game.addPlayer(player);

        command = new CreateCardHandler();
    }

    @Test
    void testParametersValid() {
        CreateCardArguments args = new CreateCardArguments(command.getCommandInfo());

        args.fromString("#" + command.getCommandInfo().name + " ");

        assertFalse(args.isValid());
    }
}