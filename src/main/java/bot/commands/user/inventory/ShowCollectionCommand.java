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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShowCollectionCommand extends AbstractCommand<ShowCollectionCommand.Arguments> {

    public static class Arguments {

        @Argument(metaVar = "user-id", usage = "user id to show collection for")
        String userId;

        @Option(name = "-series", aliases = {"-s"})
        String filterSeries;

        @Option(name = "-name", aliases = {"-n"})
        String filterName;

        @Option(name = "-o")
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
        List<CardPersonal> cards =
                sortCards(
                        collection.getCards(
                                requestedPlayer.getId(),
                                commandArgs.filterName,
                                commandArgs.filterSeries
                        ).stream()
                ).collect(Collectors.toList());

        SimpleMenuCreator.showMenuForPersonalCardStats(cards, event, game);
    }

    private Stream<CardPersonal> sortCards(Stream<CardPersonal> cardStream) {
        if (commandArgs.sortingTypes.isEmpty()){
            return cardStream;
        }

        Comparator<CardPersonal> comparator = getCardComparators(commandArgs.sortingTypes);

        return cardStream.sorted(comparator);
    }

    private Comparator<CardPersonal> getCardComparators(List<SortingType> sortingTypes) {
        Comparator<CardPersonal> comparator = getComparator(sortingTypes.get(0));
        for (int i = 1; i < sortingTypes.size(); i++) {
            comparator.thenComparing(getComparator(sortingTypes.get(1)));
        }
        return comparator;
    }

    private Comparator<CardPersonal> getComparator(SortingType sortingType) {
        if (sortingType == SortingType.FAVOR ||
                sortingType == SortingType.F){

            return Comparator.comparing(CardPersonal::getApprovalRating);

        } else if (sortingType == SortingType.PRINT ||
                sortingType == SortingType.P){

            return Comparator.comparing(CardPersonal::getApprovalRating);
        } else {
            return Comparator.comparing(CardPersonal::getCardId);
        }
    }
}
