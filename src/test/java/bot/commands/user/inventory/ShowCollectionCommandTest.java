package bot.commands.user.inventory;

import bot.commands.SortingType;
import bot.commands.user.shop.MessageSenderTester;
import game.cards.CardPersonal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ShowCollectionCommandTest extends MessageSenderTester {

    @Test
    void testNoCardsFound() {
        List<CardPersonal> result = game().getCardsPersonal().getFiltered(
                "unknown", "unknown"
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void testFilterByNameCollection() {
        List<CardPersonal> filtered = game().getCardsPersonal().getFiltered(
                "r", null
        );

        for (CardPersonal card : filtered){
            assertTrue(card.getName().toLowerCase().contains("r"));
        }
    }

    @Test
    void testFilterBySeriesCollection() {
        List<CardPersonal> filtered = game().getCardsPersonal().getFiltered(
                null, "made"
        );

        for (CardPersonal card : filtered){
            assertTrue(card.getSeriesName().toLowerCase().contains("made"));
        }
    }

    @Test
    void testFilterByNameAndSeriesCollection() {
        List<CardPersonal> filtered = game().getCardsPersonal().getFiltered(
                "r", "made"
        );

        for (CardPersonal card : filtered){
            assertTrue(card.getName().toLowerCase().contains("r"));
            assertTrue(card.getSeriesName().toLowerCase().contains("made"));
        }
    }

    @Test
    void testCollectionFavorSorting() {
        List<CardPersonal> collection = game().getCardsPersonal()
                .getCardsSorted(List.of(SortingType.FAVOR));

        for (int i = 1; i < collection.size(); i++) {
            assertTrue(collection.get(i - 1).getApprovalRating() >= collection.get(i).getApprovalRating());
        }
    }

    @Test
    void testCollectionPrintSorting() {
        List<CardPersonal> collection = game().getCardsPersonal()
                .getCardsSorted(List.of(SortingType.PRINT));

        for (int i = 1; i < collection.size(); i++) {
            assertTrue(collection.get(i - 1).getPrint() <= collection.get(i).getPrint());
        }
    }
}