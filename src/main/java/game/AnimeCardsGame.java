package game;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.cards.CardsGlobalManager;
import game.cards.CardsPersonalManager;
import game.items.ItemsPersonalManager;
import game.items.ItemGlobal;
import game.items.ItemsGlobalManager;
import game.items.MaterialsSet;
import game.shop.ArmorShop;
import game.shop.ItemsShop;
import bot.commands.MenuCreator;
import game.squadron.PatrolType;
import game.squadron.Squadron;
import game.stocks.StocksManager;
import game.stocks.StocksPersonal;
import game.wishlist.WishList;
import game.wishlist.WishListsManager;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AnimeCardsGame {
    private final EventWaiter eventWaiter;

    private final ItemsShop itemsShop;
    private final ArmorShop armorShop;

    private final List<Player> players = new ArrayList<>();
    CardsGlobalManager cardsGlobalManager;
    ItemsGlobalManager itemsGlobalManager;

    CardsPersonalManager cardsPersonalManager;
    ItemsPersonalManager itemsPersonalManager;
    StocksManager stocksManager;
    WishListsManager wishListsManager;

    private final List<PatrolActivity> currentPatrols;


    public AnimeCardsGame(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
        cardsGlobalManager = new CardsGlobalManager();
        itemsGlobalManager = new ItemsGlobalManager();
        itemsShop = new ItemsShop(itemsGlobalManager);
        armorShop = new ArmorShop(itemsGlobalManager);

        cardsPersonalManager = new CardsPersonalManager();
        itemsPersonalManager = new ItemsPersonalManager();

        currentPatrols = new LinkedList<>();
    }

    public EventWaiter getEventWaiter() {
        return eventWaiter;
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
        cardsGlobalManager.addCard(card);
    }

    public CardGlobal getCardGlobalById(String id) {
        return cardsGlobalManager.getCardById(id);
    }

    public CardGlobal getCardGlobal(String cardName, String series) {
        return cardsGlobalManager.getCardByNameAndSeries(cardName, series);
    }

    public List<CardGlobal> getMatchingCardsGlobal(String name, String series) {
        return cardsGlobalManager.getAllCardsByNameAndSeries(name, series);
    }

    public void removeCardById(String id) {
        cardsGlobalManager.removeCardById(id);
    }

    public CardsGlobalManager getCollection() {
        return cardsGlobalManager;
    }

    public Player createNewPlayer(String playerId) {
        Player player = new Player(
                playerId,
                cardsPersonalManager,
                new MaterialsSet(),
                itemsPersonalManager
        );
        addPlayer(player);
        return player;
    }

    public void pickPersonalCardDelay(Player player, String globalCardId, float pickDelay) {
        CardGlobal cardGlobal = getCardGlobalById(globalCardId);
        CardPersonal card = getPersonalCardForDelay(cardGlobal, pickDelay);
        player.addCard(card);
    }

    private CardPersonal getPersonalCardForDelay(CardGlobal card, float delay) {
        return new CardPersonal(card.getCharacterInfo(), card.getStats().getStatsForPickDelay(delay));
    }

    public CardsPersonalManager getCardsPersonalManager(Player player) {
        if (player != null){
            return player.getCardsManager();
        }
        return null;
    }

    public StocksManager getSeriesStocksManager() {
        return stocksManager;
    }

    public ItemGlobal addItem(ItemGlobal item) {
        itemsGlobalManager.addItem(item);
        return item;
    }

    public boolean removeItemById(String itemId) {
        return itemsGlobalManager.removeById(itemId);
    }

    public Paginator getItemShopViewer(User user) {
        return MenuCreator.getShopMenu(eventWaiter, itemsShop, user);
    }

    public Paginator getArmorShopViewer(User user) {
        return MenuCreator.getShopMenu(eventWaiter, armorShop, user);
    }

    public ItemsGlobalManager getItemsCollection() {
        return itemsGlobalManager;
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

    public CardPersonal getCardPersonal(Player player, String cardId) {
        return player.getCardsManager().getCardById(cardId);
    }

    public boolean createNewPatrol(Player player, PatrolType patrolType) {
        Squadron squadron = player.getSquadron();
        if (squadron != null){
            PatrolActivity patrol = new PatrolActivity(squadron, patrolType);
            startPatrol(patrol);
            return true;
        }

        return false;
    }

    private void startPatrol(PatrolActivity patrol) {
        currentPatrols.add(patrol);
    }

    private void finishOldestPatrol(){
        if (currentPatrols.isEmpty()){
            return;
        }

        PatrolActivity patrol = currentPatrols.get(0);
        finishPatrol(patrol);
        currentPatrols.remove(0);
    }

    public MaterialsSet finishPatrol(PatrolActivity patrol) {
        Player player = getPlayerById(patrol.getSquadron().getPlayerId());
        MaterialsSet materials = patrol.getMaterialsFound();

        player.getMaterials().addMaterials(materials);
        patrol.setFinished(true);

        return materials;
    }

    public PatrolActivity findPatrol(Player player) {
        return currentPatrols.stream()
                .filter(patrol -> patrol.getSquadron().getPlayerId().equals(player.getId()))
                .findFirst().orElse(null);
    }

    public float exchangeCardForStock(Player player, CardPersonal card) {
        float cardStockValue = stocksManager.getCardStockValue(card);
        if (cardsPersonalManager.removeCard(player.getId(), card)){
            String seriesTitle = card.getCharacterInfo().getSeriesTitle();
            stocksManager.addStockValue(player.getId(), seriesTitle, cardStockValue);
            return cardStockValue;
        }else{
            return 0;
        }
    }

    public StocksPersonal getStocks(String playerId) {
        return stocksManager.getElement(playerId);
    }

    public void addToWishlist(Player player, CardGlobal card) {
        wishListsManager.addCard(player.getId(), card);
    }

    public boolean removeFromWishlist(Player player, CardGlobal card) {
        return wishListsManager.removeCard(player.getId(), card);
    }

    public @NotNull WishList getWishList(String playerId) {
        return wishListsManager.getElement(playerId);
    }
}

