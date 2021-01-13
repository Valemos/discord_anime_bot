package game;

import bot.CommandPermissions;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.cards.CardCollectionGlobal;
import game.cards.CardCollectionsPersonal;
import game.items.ItemCollectionsPersonal;
import game.items.ItemGlobal;
import game.items.ItemCollectionGlobal;
import game.items.MaterialsSet;
import game.shop.ArmorShop;
import game.shop.ItemsShop;
import game.shop.ShopViewer;
import game.squadron.Squadron;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class AnimeCardsGame {
    private final EventWaiter eventWaiter;

    private final ItemsShop itemsShop;
    private final ArmorShop armorShop;

    private final List<Player> players = new ArrayList<>();
    CardCollectionGlobal cardCollectionGlobal;
    ItemCollectionGlobal itemCollectionGlobal;

    CardCollectionsPersonal cardCollectionsPersonal;
    ItemCollectionsPersonal itemCollectionsPersonal;

    public AnimeCardsGame(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
        cardCollectionGlobal = new CardCollectionGlobal();
        itemCollectionGlobal = new ItemCollectionGlobal();
        itemsShop = new ItemsShop(itemCollectionGlobal);
        armorShop = new ArmorShop(itemCollectionGlobal);
    }

    public Player getPlayerById(String playerId) {
        return players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseGet(() -> createNewPlayer(playerId));
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addCard(CardGlobal card) {
        cardCollectionGlobal.addCard(card);
    }

    public CardGlobal getGlobalCardById(String id) {
        return cardCollectionGlobal.getCardById(id);
    }

    public CardGlobal getGlobalCard(String cardName, String series) {
        return cardCollectionGlobal.getCardByNameAndSeries(cardName, series);
    }

    public void removeCardById(String id) {
        cardCollectionGlobal.removeCardById(id);
    }

    public CardCollectionGlobal getCollection() {
        return cardCollectionGlobal;
    }

    public Player createNewPlayer(String playerId) {
        return createNewPlayer(playerId, CommandPermissions.USER);
    }

    public Player createNewPlayer(String playerId, CommandPermissions accessLevel) {
        Player player = new Player(
                playerId,
                accessLevel,
                cardCollectionsPersonal,
                new MaterialsSet(),
                itemCollectionsPersonal
        );
        addPlayer(player);
        return player;
    }

    public void pickPersonalCardDelay(Player player, String globalCardId, float pickDelay) {
        CardGlobal cardGlobal = getGlobalCardById(globalCardId);
        CardPersonal card = getPersonalCardForDelay(cardGlobal, pickDelay);
        player.addPersonalCard(card);
    }

    private CardPersonal getPersonalCardForDelay(CardGlobal card, float delay) {
        return new CardPersonal(card.getCharacterInfo(), card.getStats().getStatsForPickDelay(delay));
    }

    public CardCollectionsPersonal getPlayerCollection(Player player) {
        if (player != null){
            return player.getCardsCollection();
        }
        return null;
    }

    public ItemGlobal addItem(ItemGlobal item) {
        itemCollectionGlobal.addItem(item);
        return item;
    }

    public boolean removeItemById(String itemId) {
        return itemCollectionGlobal.removeById(itemId);
    }

    public Paginator getItemShopViewer(User user) {
        return ShopViewer.get(eventWaiter, itemsShop, user);
    }

    public Paginator getArmorShopViewer(User user) {
        return ShopViewer.get(eventWaiter, armorShop, user);
    }

    public ItemCollectionGlobal getItemsCollection() {
        return itemCollectionGlobal;
    }

    public ItemsShop getItemsShop() {
        return itemsShop;
    }

    public Squadron getSquadron(Player player) {
        Squadron squadron = player.getSquadron();

        if (squadron == null){
            squadron = new Squadron(3);
            player.setSquadron(squadron);
        }

        return squadron;
    }

    public CardPersonal getPersonalCard(Player player, String cardId) {
        return player.getCardsCollection().getCardById(cardId);
    }
}

