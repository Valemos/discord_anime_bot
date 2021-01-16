package game;

import bot.menu.SimpleMenuCreator;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.cards.*;
import game.contract.CardForCardContract;
import game.contract.ContractsManager;
import game.contract.MultiTradeContract;
import game.contract.SendCardsContract;
import game.items.*;
import game.shop.ArmorShop;
import game.shop.ItemsShop;
import game.squadron.PatrolActivity;
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

    private ItemsShop itemsShop;
    private ArmorShop armorShop;

    private List<Player> players;
    private CardsGlobalManager cardsGlobalManager;
    private ItemsGlobalManager itemsGlobalManager;
    private CardsPersonalManager cardsPersonalManager;
    private ItemsPersonalManager itemsPersonalManager;
    private StocksManager stocksManager;
    private WishListsManager wishListsManager;
    private MaterialsManager materialsManager;

    private ContractsManager contractsManager;
    private List<PatrolActivity> currentPatrols;
    private CardDropManager cardDropManager;


    public AnimeCardsGame(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
        reset();
    }

    public void reset(){
        players = new ArrayList<>();
        cardsGlobalManager = new CardsGlobalManager();
        itemsGlobalManager = new ItemsGlobalManager();

        cardsPersonalManager = new CardsPersonalManager();
        itemsPersonalManager = new ItemsPersonalManager();
        stocksManager = new StocksManager();
        wishListsManager = new WishListsManager();
        materialsManager = new MaterialsManager();

        currentPatrols = new LinkedList<>();

        itemsShop = new ItemsShop(itemsGlobalManager);
        armorShop = new ArmorShop(itemsGlobalManager);

        contractsManager = new ContractsManager(List.of(
                SendCardsContract.class,
                CardForCardContract.class,
                MultiTradeContract.class
        ));
        cardDropManager = new CardDropManager();
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

    public List<Player> getAllPlayers() {
        return players;
    }

    public void addCard(CardGlobal card) {
        cardsGlobalManager.addCard(card);
    }

    public CardGlobal getCardGlobalById(String id) {
        return cardsGlobalManager.getCardById(id);
    }

    public CardGlobal getCardGlobal(String cardName, String series) {
        return cardsGlobalManager.getFirstCard(cardName, series);
    }

    public CardGlobal getCardGlobalUnique(String name, String series) {
        List<CardGlobal> cards = cardsGlobalManager.getAllCards(name, series);
        if (cards.size() == 1){
            return cards.get(0);
        }
        return null;
    }


    public List<CardGlobal> getMatchingCardsGlobal(String name, String series) {
        return cardsGlobalManager.getAllCards(name, series);
    }

    public void removeCardById(String id) {
        cardsGlobalManager.removeCardById(id);
    }

    public CardsGlobalManager getCardsGlobalManager() {
        return cardsGlobalManager;
    }

    public Player createNewPlayer(String playerId) {
        Player player = new Player(
                playerId,
                cardsPersonalManager,
                itemsPersonalManager,
                materialsManager
        );
        addPlayer(player);
        return player;
    }

    public CardPersonal pickPersonalCardDelay(Player player, String globalCardId, float pickDelay) {
        CardGlobal cardGlobal = getCardGlobalById(globalCardId);
        if (cardGlobal == null){
            return null;
        }

        cardGlobal.getStats().incrementCardPrint();
        CardPersonal card = getPersonalCardForDelay(cardGlobal, pickDelay);
        player.addCard(card);
        return card;
    }

    private CardPersonal getPersonalCardForDelay(CardGlobal card, float delay) {
        return new CardPersonal(card.getCharacterInfo(), card.getStats().getStatsForPickDelay(delay));
    }

    public CardsPersonalManager getCardsPersonalManager() {
        return cardsPersonalManager;
    }

    public CardPersonal getCardPersonal(String playerId, String cardId) {
        CardPersonal card = cardsPersonalManager.getCardById(cardId);
        if(playerId.equals(card.getPlayerId())){
            return card;
        }

        return null;
    }


    public ItemGlobal addItem(ItemGlobal item) {
        itemsGlobalManager.addItem(item);
        return item;
    }

    public boolean removeItemById(String itemId) {
        return itemsGlobalManager.removeById(itemId);
    }

    public Paginator getItemShopViewer(User user) {
        return SimpleMenuCreator.getShopMenu(eventWaiter, itemsShop, user);
    }

    public Paginator getArmorShopViewer(User user) {
        return SimpleMenuCreator.getShopMenu(eventWaiter, armorShop, user);
    }

    public ItemsGlobalManager getItemsGlobal() {
        return itemsGlobalManager;
    }

    public ItemsPersonalManager getItemsPersonal() {
        return itemsPersonalManager;
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

        if (cardsPersonalManager.removeCard(player.getId(), card.getCardId())){
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


    public ContractsManager getContractsManager() {
        return contractsManager;
    }

    public String getCardPersonalOwner(String receiveCardId) {
        return cardsPersonalManager.getCardById(receiveCardId).getPlayerId();
    }

    public boolean isPlayerExists(String id) {
        return getPlayerById(id) != null;
    }

    public CardDropManager getDropManager() {
        return cardDropManager;
    }
}

