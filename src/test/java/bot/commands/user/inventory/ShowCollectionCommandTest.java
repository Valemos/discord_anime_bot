package bot.commands.user.inventory;

import bot.BotMessageSenderMock;
import bot.commands.SortingType;
import game.cards.CardPersonal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShowCollectionCommandTest {


    static BotMessageSenderMock sender;

    @BeforeAll
    static void setSender() throws Exception {
        sender = new BotMessageSenderMock();
    }

    @BeforeEach
    void setUp() {
        sender.reset();
    }

    @Test
    void testNoCardsFound() {
        List<CardPersonal> result = sender.getGame().getCardsPersonal().getFiltered(
                "unknown", "unknown"
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void testFilterByNameCollection() {
        List<CardPersonal> filtered = sender.getGame().getCardsPersonal().getFiltered(
                "r", null
        );

        for (CardPersonal card : filtered){
            assertTrue(card.getName().toLowerCase().contains("r"));
        }
    }

    @Test
    void testFilterBySeriesCollection() {
        List<CardPersonal> filtered = sender.getGame().getCardsPersonal().getFiltered(
                null, "made"
        );

        for (CardPersonal card : filtered){
            assertTrue(card.getSeries().toLowerCase().contains("made"));
        }
    }

    @Test
    void testFilterByNameAndSeriesCollection() {
        List<CardPersonal> filtered = sender.getGame().getCardsPersonal().getFiltered(
                "r", "made"
        );

        for (CardPersonal card : filtered){
            assertTrue(card.getName().toLowerCase().contains("r"));
            assertTrue(card.getSeries().toLowerCase().contains("made"));
        }
    }

    @Test
    void testCollectionFavorSorting() {
        List<CardPersonal> collection = sender.getGame().getCardsPersonal()
                .getCardsSorted(List.of(SortingType.FAVOR));

        for (int i = 1; i < collection.size(); i++) {
            assertTrue(collection.get(i - 1).getApprovalRating() >= collection.get(i).getApprovalRating());
        }
    }

    @Test
    void testCollectionPrintSorting() {
        List<CardPersonal> collection = sender.getGame().getCardsPersonal()
                .getCardsSorted(List.of(SortingType.PRINT));

        for (int i = 1; i < collection.size(); i++) {
            assertTrue(collection.get(i - 1).getPrint() <= collection.get(i).getPrint());
        }
    }
}