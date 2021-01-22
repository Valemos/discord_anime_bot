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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import javax.lang.model.util.Types;
import javax.security.auth.login.LoginException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class BuyCommandTest {

    private static BotMessageSenderMock sender;
    private ItemsShop spyItemsShop;
    private List<? extends AbstractShopItem> items;
    private ArgumentCaptor<AbstractShopItem> itemCaptor;

    @BeforeAll
    static void beforeAll() throws LoginException {
        sender = new BotMessageSenderMock();
    }

    @BeforeEach
    void setUp() {
        sender.reset();

        spyItemsShop = spy(sender.getGame().getItemsShop());
        doReturn(spyItemsShop).when(sender.getGame()).getItemsShop();
        items = spyItemsShop.getItems();

        itemCaptor = ArgumentCaptor.forClass(AbstractShopItem.class);
    }

    @Test
    void testItemBought() {
        sender.send("#buy 1");

        verify(spyItemsShop, atLeastOnce()).tryBuyItem(any(Player.class), itemCaptor.capture());
        assertSame(items.get(0), itemCaptor.getValue());
    }

    @Test
    void testItemNotFound() {
        sender.send("#buy 100");
        verify(spyItemsShop, never()).tryBuyItem(any(Player.class), any(AbstractShopItem.class));
    }
}