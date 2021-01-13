package bot.commands.user;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import org.kohsuke.args4j.Argument;

import java.util.List;
import java.util.stream.Collectors;

public class SeriesValueCommand extends AbstractCommand<SeriesValueCommand.Arguments> {

    public static class Arguments{
        @Argument(usage = "series name to search for")
        List<String> seriesNameWords;
    }

    public SeriesValueCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
    }

    @Override
    protected void handle(CommandEvent event) {
        String seriesName = String.join(" ", commandArgs.seriesNameWords);

    }
}
