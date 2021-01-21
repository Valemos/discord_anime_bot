package bot.commands.user.shop;

import bot.BotAnimeCards;
import bot.BotMessageSenderMock;
import game.AnimeCardsGame;
import game.Player;
import game.shop.ItemsShop;
import game.shop.items.AbstractShopItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.LoginException;

import java.util.List;

import static org.mockito.Mockito.*;

class BuyCommandTest {

    private static BotMessageSenderMock sender;
    private AnimeCardsGame spyGame;
    private ItemsShop spyItemsShop;
    private List<? extends AbstractShopItem> items;

    @BeforeAll
    static void beforeAll() throws LoginException {
        sender = new BotMessageSenderMock();
    }

    @BeforeEach
    void setUp() {
        sender.reset();
        spyGame = sender.getGame();
        spyItemsShop = spy(spyGame.getItemsShop());
        items = spyItemsShop.getItems();
        doReturn(spyItemsShop).when(spyGame).getItemsShop();
    }

    @Test
    void testItemBought() {
        sender.send("#buy 1");

        verify(spyItemsShop).tryBuyItem(any(Player.class), items.get(0));
    }
}