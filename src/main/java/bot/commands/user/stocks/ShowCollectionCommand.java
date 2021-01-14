package bot.commands.user.stocks;

import bot.commands.AbstractCommand;
import bot.commands.SortingType;
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

        @Argument(usage = "user id to show collection for")
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
    protected void handle(CommandEvent event) {

        Player requestedPlayer = commandArgs.userId != null ? game.getPlayerById(commandArgs.userId) : player;

        if (requestedPlayer == null){
            event.getChannel().sendMessage("player not found").queue();
            return;
        }

        CardsPersonalManager collection = game.getCardsPersonalManager();
        List<CardPersonal> cards =
                sortCards(
                        filterCards(
                                collection.getCards(requestedPlayer.getId())
                        )
                ).collect(Collectors.toList());
        displayCards(event, cards);
    }

    private Stream<CardPersonal> sortCards(Stream<CardPersonal> cardStream) {
        if (commandArgs.sortingTypes.isEmpty()){
            return cardStream;
        }

        Comparator<CardPersonal> comparator = getCardComparatorFromList(commandArgs.sortingTypes);

        return cardStream.sorted(comparator);
    }

    private Comparator<CardPersonal> getCardComparatorFromList(List<SortingType> sortingTypes) {
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

    private Stream<CardPersonal> filterCards(List<CardPersonal> cards) {
        Stream<CardPersonal> cardStream = cards.stream();

        if (commandArgs.filterName != null){
            cardStream = cardStream.filter((card) -> card.getName().equals(commandArgs.filterName));
        }

        if (commandArgs.filterSeries != null){
            cardStream = cardStream.filter((card) -> card.getSeries().equals(commandArgs.filterSeries));
        }

        return cardStream;
    }

    private void displayCards(CommandEvent event, List<CardPersonal> cards) {
        StringBuilder b = new StringBuilder();
        b.append("User collection");
        int counter = 1;
        for (CardPersonal card : cards){
            b.append('\n').append(counter++).append('.');
            b.append(card.getOneLineRepresentationString());
        }

        event.getChannel().sendMessage(b.toString()).queue();
    }
}
