package bot.commands.admin;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import org.kohsuke.args4j.Argument;

public class GrabTimeCommand extends AbstractCommand<GrabTimeCommand.Arguments> {

    public static class Arguments{
        @Argument(metaVar = "seconds", required = true)
        int seconds;
    }

    public GrabTimeCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "grabtime";
        requiredRole = "admin";
    }

    @Override
    public void handle(CommandEvent event) {

    }
}
