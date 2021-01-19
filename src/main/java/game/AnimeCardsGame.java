package game;

import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.cards.*;
import game.contract.CardForCardContract;
import game.contract.ContractsManager;
import game.contract.MultiTradeContract;
import game.contract.SendCardsContract;
import game.cooldown.CooldownManager;
import game.cooldown.CooldownSet;
import game.items.*;
import game.shop.ArmorShop;
import game.shop.ItemsShop;
import game.squadron.PatrolActivity;
import game.squadron.PatrolType;
import game.squadron.Squadron;
import game.stocks.StockValue;
import game.wishlist.WishList;
import game.wishlist.WishListsManager;
import net.dv8tion.jda.api.entities.User;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

public class AnimeCardsGame {
    private final EventWaiter eventWaiter;
    private final Session dbSession;

    private ItemsShop itemsShop;
    private ArmorShop armorShop;

    private CardsGlobalManager cardsGlobalManager;
    private ItemsGlobalManager itemsGlobalManager;
    private CardsPersonalManager cardsPersonalManager;
    private ItemsPersonalManager itemsPersonalManager;
    private WishListsManager wishListsManager;

    private ContractsManager contractsManager;
    private List<PatrolActivity> currentPatrols;
    private CardDropManager cardDropManager;
    private CooldownManager cooldownManager;


    public AnimeCardsGame(EventWaiter eventWaiter, Session dbSession) {
        this.eventWaiter = eventWaiter;
        this.dbSession = dbSession;
        reset(dbSession);
    }

    public void reset(Session dbSession){
        cardsGlobalManager = new CardsGlobalManager(dbSession);
        itemsGlobalManager = new ItemsGlobalManager(dbSession);

        cardsPersonalManager = new CardsPersonalManager(dbSession);
        itemsPersonalManager = new ItemsPersonalManager(dbSession);
        wishListsManager = new WishListsManager(dbSession);

        currentPatrols = new LinkedList<>();

        itemsShop = new ItemsShop(itemsGlobalManager);
        armorShop = new ArmorShop(itemsGlobalManager);

        contractsManager = new ContractsManager(dbSession, List.of(
                SendCardsContract.class,
                CardForCardContract.class,
                MultiTradeContract.class
        ));
        cardDropManager = new CardDropManager(dbSession);
        cooldownManager = new CooldownManager(dbSession);
    }

    public Session getDatabaseSession() {
        return dbSession;
    }

    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public Player getPlayer(String playerId) {
        Player player = dbSession.get(Player.class, playerId);
        return player != null ? player : createNewPlayer(playerId);
    }

    public Player createNewPlayer(String playerId) {
        Player player = new Player();
        player.setId(playerId);
        dbSession.saveOrUpdate(player);
        return player;
    }

    public List<Player> getAllPlayers() {

        CriteriaBuilder cb = dbSession.getCriteriaBuilder();
        CriteriaQuery<Player> cq = cb.createQuery(Player.class);
        Root<Player> root = cq.from(Player.class);
        CriteriaQuery<Player> playersQuery = cq.select(root);

        return dbSession.createQuery(playersQuery).list();
    }

    public CardsPersonalManager getCardsPersonal() {
        return cardsPersonalManager;
    }

    public void addCard(CardGlobal card) {
        cardsGlobalManager.addCard(card);
    }

    public CardsGlobalManager getCardsGlobal(){
        return cardsGlobalManager;
    }

    public CardGlobal getCardGlobal(String cardName, String series) {
        return cardsGlobalManager.getFirstCard(cardName, series);
    }

    public CardGlobal getCardGlobalUnique(String name, String series) {
        List<CardGlobal> cards = cardsGlobalManager.getFilteredCards(name, series);
        if (cards.size() == 1){
            return cards.get(0);
        }
        return null;
    }

    public void removeCardById(String id) {
        cardsGlobalManager.removeCardById(id);
    }

    public CardPersonal pickPersonalCard(Player player, CardGlobal cardGlobal, float pickDelay) {
        cardGlobal.getStats().incrementCardPrint();
        CardPersonal card = getPersonalCardForDelay(cardGlobal, pickDelay);
        player.addCard(card);
        return card;
    }

    private CardPersonal getPersonalCardForDelay(CardGlobal card, float delay) {
        return new CardPersonal(card.getCharacterInfo(), card.getStats().getStatsForPickDelay(delay));
    }

    public ItemGlobal addItem(ItemGlobal item) {
        itemsGlobalManager.addItem(item);
        return item;
    }

    public boolean removeItemById(String itemId) {
        return itemsGlobalManager.removeById(itemId);
    }

    public Paginator getItemShopViewer(User user) {
        return BotMenuCreator.menuShop(eventWaiter, itemsShop, user);
    }

    public Paginator getArmorShopViewer(User user) {
        return BotMenuCreator.menuShop(eventWaiter, armorShop, user);
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
//        Squadron squadron = player.getSquadron();
//
//        if (squadron == null){
//            squadron = new Squadron(3);
//            player.setSquadron(squadron);
//        }

        // TODO add squadrons table

        return null;
    }

    public boolean createNewPatrol(Player player, PatrolType patrolType) {
        Squadron squadron = getSquadron(player);
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
        Player player = getPlayer(patrol.getSquadron().getPlayerId());
        MaterialsSet materials = patrol.getMaterialsFound();

        player.addMaterials(materials);
        patrol.setFinished(true);

        return materials;
    }

    public PatrolActivity findPatrol(Player player) {
        return currentPatrols.stream()
                .filter(patrol -> patrol.getSquadron().getPlayerId().equals(player.getId()))
                .findFirst().orElse(null);
    }

    public float exchangeCardForStock(CardPersonal card) {
        dbSession.beginTransaction();

        StockValue cardStockValue = new StockValue(card);
        dbSession.save(cardStockValue);

        cardsPersonalManager.removeCard(card);

        dbSession.getTransaction().commit();
        return cardStockValue.getValue();
    }

    public void addToWishlist(Player player, CardGlobal card) {
        wishListsManager.addCard(player.getId(), card);
    }

    public boolean removeFromWishlist(Player player, CardGlobal card) {
        return wishListsManager.removeCard(player.getId(), card);
    }

    public @NotNull WishList getWishList(String playerId) {
        return wishListsManager.getElementOrCreate(playerId);
    }


    public ContractsManager getContractsManager() {
        return contractsManager;
    }

    public Player getCardPersonalOwner(String cardId) {
        return dbSession.get(CardPersonal.class, cardId).getOwner();
    }

    public boolean isPlayerExists(String id) {
        return getPlayer(id) != null;
    }

    public CardDropManager getDropManager() {
        return cardDropManager;
    }

    public CooldownSet getCooldowns(String playerId) {
        return cooldownManager.getElementOrCreate(playerId);
    }
}

