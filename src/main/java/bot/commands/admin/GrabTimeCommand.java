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
        help = "used to set #drop timeout";
    }

    @Override
    public void handle(CommandEvent event) {
        game.getDropManager().setFightSeconds(commandArgs.seconds);
        sendMessage(event, "from now all card fights will have timeout of **" + commandArgs.seconds + "** seconds");
    }
}
