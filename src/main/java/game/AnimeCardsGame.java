package game;

import bot.BotAnimeCards;
import bot.CommandPermissions;
import com.jagrosh.jdautilities.menu.Paginator;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.cards.CardCollectionGlobal;
import game.cards.CardCollectionPersonal;
import game.items.ItemGlobal;
import game.items.ItemCollectionGlobal;
import game.shop.ArmorShop;
import game.shop.ItemsShop;
import game.shop.ShopViewer;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class AnimeCardsGame {
    private final List<Player> players = new ArrayList<>();

    CardCollectionGlobal cardCollectionGlobal;
    ItemCollectionGlobal itemCollectionGlobal;
    private final ItemsShop itemsShop;
    private final ArmorShop armorShop;

    private final BotAnimeCards mainBot;

    public AnimeCardsGame(BotAnimeCards bot) {
        mainBot = bot;
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

    private void removePlayer(Player player) {
        players.removeIf((p) -> p.getId().equals(player.getId()));
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

    public List<CardGlobal> getMatchingCards(String name, String series) {
        return cardCollectionGlobal.getAllCardsByNameAndSeries(name, series);
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
        Player player = new Player(playerId, accessLevel);
        addPlayer(player);
        return player;
    }

    public void pickPersonalCardDelay(Player player, String globalCardId, float pickDelay) {
        CardGlobal cardGlobal = getGlobalCardById(globalCardId);
        CardPersonal cardPersonal = cardGlobal.getPersonalCardForDelay(player.getId(), pickDelay);
        player.addPersonalCard(cardPersonal);
    }

    public CardCollectionPersonal getPlayerCollection(Player player) {
        if (player != null){
            return player.getCollection();
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
        return ShopViewer.get(mainBot.getEventWaiter(), itemsShop, user);
    }

    public Paginator getArmorShopViewer(User user) {
        return ShopViewer.get(mainBot.getEventWaiter(), armorShop, user);
    }

    public ItemCollectionGlobal getItemsCollection() {
        return itemCollectionGlobal;
    }

    public ItemsShop getItemsShop() {
        return itemsShop;
    }
}

