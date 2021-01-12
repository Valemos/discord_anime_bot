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
import game.shop.ItemShop;
import game.shop.ShopViewer;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public class AnimeCardsGame {
    private final List<Player> players = new ArrayList<>();

    CardCollectionGlobal cardCollectionGlobal;
    ItemCollectionGlobal itemCollectionGlobal;
    private final ItemShop itemShopGlobal;

    private final BotAnimeCards mainBot;

    public AnimeCardsGame(BotAnimeCards bot) {
        mainBot = bot;
        cardCollectionGlobal = new CardCollectionGlobal();
        itemCollectionGlobal = new ItemCollectionGlobal();
        itemShopGlobal = new ItemShop(itemCollectionGlobal);
    }

    public Player getPlayerById(String playerId) {
        return players.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst().orElse(null);
    }

    public void addPlayer(Player player) {
        removePlayer(player);
        players.add(player);
    }

    private void removePlayer(Player player) {
        players.removeIf((p) -> p.getId().equals(player.getId()));
    }

    public void addCard(CardGlobal card) {
        cardCollectionGlobal.addCard(card);
    }

    public CardGlobal getGlobalCardById(int id) {
        return cardCollectionGlobal.getCardById(id);
    }

    public CardGlobal getGlobalCardByName(String cardName) {
        return cardCollectionGlobal.getCardByName(cardName);
    }

    public void removeCardById(int id) {
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

    public void pickPersonalCardDelay(Player player, int globalCardId, float pickDelay) {
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
        // TODO add item system
        return item;
    }

    public boolean removeItemById(String itemId) {
        return itemCollectionGlobal.removeById(itemId);
    }

    public Paginator getShopViewer(User user) {
        return ShopViewer.buildPagesMenu(mainBot.getEventWaiter(), this, user);
    }

    public ItemCollectionGlobal getItemsCollection() {
        return itemCollectionGlobal;
    }

    public ItemShop getItemShop() {
        return itemShopGlobal;
    }
}

