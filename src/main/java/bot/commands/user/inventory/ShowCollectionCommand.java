package bot.commands.user.inventory;

import bot.commands.AbstractCommand;
import bot.commands.SortingType;
import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CollectionTransformer;
import game.cards.CardPersonal;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

public class ShowCollectionCommand extends AbstractCommand<ShowCollectionCommand.Arguments> {

    public static class Arguments {

        @Argument(metaVar = "user id", usage = "user id to show collection for")
        String userId;

        @Option(metaVar = "name", name = "-name", aliases = {"-n"}, usage = "name to filter collection")
        String filterName;

        @Option(metaVar = "series", name = "-series", aliases = {"-s"}, usage = "series name to filter collection")
        String filterSeries;

        @Option(metaVar = "sorting type", name = "-o", usage = "ordering options")
        List<SortingType> sortingTypes = new ArrayList<>();
    }

    public ShowCollectionCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "collection";
        aliases = new String[]{"c"};
        help = "all multi word arguments must be in quotes (\" \")\n" + help;
    }

    @Override
    public void handle(CommandEvent event) {

        Player requestedPlayer = commandArgs.userId != null ? game.getPlayer(commandArgs.userId) : player;

        if (requestedPlayer == null){
            event.getChannel().sendMessage("player not found").queue();
            return;
        }

        List<CardPersonal> cards = new CollectionTransformer<>(requestedPlayer.getCards().stream())
                .filterName(commandArgs.filterName)
                .filterSeries(commandArgs.filterSeries)
                .sort(commandArgs.sortingTypes)
                .toList();

        BotMenuCreator.menuForPersonalCardStats(cards, event, game, 1);
    }
}
