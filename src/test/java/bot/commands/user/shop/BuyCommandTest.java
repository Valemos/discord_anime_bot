package bot.commands.user.shop;

import game.AnimeCardsGame;
import game.Player;
import game.cards.CardPersonal;
import game.materials.Material;
import game.shop.PowerUpsShop;
import game.shop.items.AbstractShopItem;
import game.shop.items.ShopPowerUp;
import game.player_objects.squadron.HealthState;
import game.player_objects.squadron.Squadron;
import game.shop.items.UsablePowerUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuyCommandTest extends MessageSenderTester{

    private PowerUpsShop spyShop;
    private List<? extends UsablePowerUp> items;
    private ArgumentCaptor<AbstractShopItem> itemCaptor;

    @BeforeEach
    void setUp() {

        spyShop = spy(game().getPowerUpsShop());
        doReturn(spyShop).when(game()).getPowerUpsShop();
        items = spyShop.getItems();

        itemCaptor = ArgumentCaptor.forClass(AbstractShopItem.class);
    }

    private void setGold(int amount) {
        tester().getMaterials().addAmount(Material.GOLD, amount);
    }

    private AbstractShopItem captureItemBought() {
        verify(spyShop, atLeastOnce()).tryBuyItem(any(AnimeCardsGame.class), any(Player.class), itemCaptor.capture());
        return itemCaptor.getValue();
    }

    @Test
    void testItemBought() {
        send("#buy 1");

        assertSame(items.get(0), captureItemBought());
    }

    @Test
    void testItemBought_byName() {
        send("#buy drop");

        captureItemBought();
        assertSame(items.get(0), captureItemBought());
    }

    @Test
    void testItemNotFound() {
        send("#buy 100");
        verify(spyShop, never()).tryBuyItem(any(AnimeCardsGame.class), any(Player.class), any(AbstractShopItem.class));
    }

    @Test
    void squadronNotExistsBuy() {
        setGold(1000);

        assertNull(tester().getSquadron());

        assertDoesNotThrow(() -> send("#buy 4"));
    }

    @Test
    void testSquadronCreatedEmpty() {
        sender.setTesterDefault(tester2());
        Squadron squadron = game().getOrCreateSquadron(tester());

        assertTrue(squadron.getMembers().isEmpty());
        assertTrue(squadron.getPowerUps().isEmpty());
    }

    @Test
    void testSquadronTheSameOnGameQuery() {
        assertSame(game().getOrCreateSquadron(tester()),
                    game().getOrCreateSquadron(tester()));
    }

    @Test
    void testSquadronHasPowerUp() {
        assertEquals(0, getSquadron().getPowerUps().size());
        setGold(1000);
        send("#buy 4");

        assertEquals(((ShopPowerUp) items.get(3)).getType(),
                tester().getSquadron().getPowerUps().get(0));
    }

    @Test
    void testSquadronNotHealed_WhenNotEnoughMoney() {
        injureSquadron(getSquadron());

        setGold(0);

        send("#buy 3");

        for(CardPersonal mem : getSquadron().getMembers()){
            assertEquals(HealthState.INJURED, mem.getHealthState());
        }
    }

    @Test
    void testSquadronHealed_whenHealthPotionBought() {
        injureSquadron(getSquadron());

        setGold(1000);
        send("#buy 3");

        for(CardPersonal mem : getSquadron().getMembers()){
            assertEquals(HealthState.HEALTHY, mem.getHealthState());
        }
    }

    private Squadron getSquadron() {
        return game().getOrCreateSquadron(tester());
    }

    private void injureSquadron(Squadron squadron) {
        for (CardPersonal mem : squadron.getMembers()){
            mem.setHealthState(HealthState.INJURED);
        }
    }
}