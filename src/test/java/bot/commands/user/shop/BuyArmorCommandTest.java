package bot.commands.user.shop;

import game.AnimeCardsGame;
import game.Player;
import game.materials.Material;
import game.shop.ArmorShop;
import game.shop.items.AbstractShopItem;
import game.shop.items.ArmorItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

class BuyArmorCommandTest extends MessageSenderTester {

    private ArmorShop spyShop;
    private List<ArmorItem> items;
    private ArgumentCaptor<AbstractShopItem> itemCaptor;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        sender.reset();

        spyShop = spy(sender.getGame().getArmorShop());
        doReturn(spyShop).when(sender.getGame()).getArmorShop();
        items = (List<ArmorItem>) spyShop.getItems();

        itemCaptor = ArgumentCaptor.forClass(AbstractShopItem.class);

        tester().getArmorItems().clear();
    }

    private void setGold(int amount) {
        tester().getMaterials().addAmount(Material.GOLD, amount);
    }

    private AbstractShopItem captureItemBought() {
        verify(spyShop, atLeastOnce()).tryBuyItem(any(AnimeCardsGame.class), any(Player.class), itemCaptor.capture());
        return itemCaptor.getValue();
    }

    @Test
    void testPlayerStartsWithNoArmor() {
        assertTrue(tester().getArmorItems().isEmpty());
    }

    @Test
    void testUnknownArmorPiece() {
        sender.send("#buyarmor 1000");
        verify(spyShop, never()).tryBuyItem(any(AnimeCardsGame.class), any(Player.class), any(AbstractShopItem.class));
    }

    @Test
    void testFindArmorById() {
        sender.send("#buyarmor " + items.get(1).getId());

        assertFalse(tester().getArmorItems().isEmpty());
        assertEquals(items.get(1), tester().getArmorItems().get(0).getOriginal());
    }

    @Test
    void testUniqueByName() {
        sender.send("#buyarmor glove");

        assertFalse(tester().getArmorItems().isEmpty());
        assertEquals(items.get(1), tester().getArmorItems().get(0).getOriginal());
    }

    @Test
    void testNoUniqueFoundAndNotBought() {
        assertTrue(tester().getArmorItems().isEmpty());
        sender.send("#buyarmor ch");
        assertTrue(tester().getArmorItems().isEmpty());
    }
}