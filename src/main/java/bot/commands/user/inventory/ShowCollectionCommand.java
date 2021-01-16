package bot.commands.user.inventory;

import bot.commands.AbstractCommand;
import bot.commands.SortingType;
import bot.menu.SimpleMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardPersonal;
import game.cards.CardsPersonalManager;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

public class ShowCollectionCommand extends AbstractCommand<ShowCollectionCommand.Arguments> {

    public static class Arguments {

        @Argument(metaVar = "user id", usage = "user id to show collection for")
        String userId;

        @Option(metaVar = "filter name", name = "-name", aliases = {"-n"})
        String filterName;

        @Option(metaVar = "filter series", name = "-series", aliases = {"-s"})
        String filterSeries;

        @Option(metaVar = "sorting type", name = "-o")
        List<SortingType> sortingTypes = new ArrayList<>();
    }

    public ShowCollectionCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "collection";
        aliases = new String[]{"c"};
    }

    @Override
    public void handle(CommandEvent event) {

        Player requestedPlayer = commandArgs.userId != null ? game.getPlayerById(commandArgs.userId) : player;

        if (requestedPlayer == null){
            event.getChannel().sendMessage("player not found").queue();
            return;
        }

        CardsPersonalManager collection = game.getCardsPersonalManager();
        List<CardPersonal> cards = collection.getCardsSorted(
                requestedPlayer.getId(),
                commandArgs.filterName,
                commandArgs.filterSeries,
                commandArgs.sortingTypes
        );

        SimpleMenuCreator.showMenuForPersonalCardStats(cards, event, game, 1);
    }
}
