package game;

import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.cards.*;
import game.contract.CardForCardContract;
import game.contract.ContractsManager;
import game.contract.MultiTradeContract;
import game.contract.SendCardsContract;
import game.materials.*;
import game.shop.ArmorShop;
import game.shop.ItemsShop;
import game.shop.items.ArmorItem;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnimeCardsGame {
    private final EventWaiter eventWaiter;
    private Session dbSession;

    private ItemsShop itemsShop;
    private ArmorShop armorShop;

    private CardsGlobalManager cardsGlobalManager;
    private CardsPersonalManager cardsPersonalManager;
    private WishListsManager wishListsManager;

    private ContractsManager contractsManager;
    private CardDropManager cardDropManager;


    public AnimeCardsGame(EventWaiter eventWaiter, Session dbSession) {
        this.eventWaiter = eventWaiter;
        this.dbSession = dbSession;
        setSession(dbSession);
    }

    public void setSession(Session dbSession){
        cardsGlobalManager = new CardsGlobalManager(dbSession);

        cardsPersonalManager = new CardsPersonalManager(dbSession);
        wishListsManager = new WishListsManager(dbSession);

        itemsShop = new ItemsShop();

        List<ArmorItem> armorItems = saveArmorItems(dbSession);
        armorShop = new ArmorShop(armorItems);


        contractsManager = new ContractsManager(dbSession, List.of(
                SendCardsContract.class,
                CardForCardContract.class,
                MultiTradeContract.class
        ));
        cardDropManager = new CardDropManager(this, dbSession);
    }

    private List<ArmorItem> saveArmorItems(Session dbSession) {
        List<ArmorItem> armorItems = getArmorItems();
        dbSession.beginTransaction();
        for(ArmorItem armor : armorItems){
            dbSession.persist(armor);
        }
        dbSession.getTransaction().commit();
        return armorItems;
    }

    private List<ArmorItem> getArmorItems() {
        // TODO add loading from json
        return List.of(
                new ArmorItem("Chainmail", 5, Map.of(Material.GOLD, 100)),
                new ArmorItem("Armored Glove", 2, Map.of(Material.GOLD, 50)),
                new ArmorItem("Dragon Chestplate", 20, Map.of(Material.GOLD, 850))
        );
    }

    public Session getDatabaseSession() {
        return dbSession;
    }

    public void setDatabaseSession(Session dbSession) {
        this.dbSession = dbSession;
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
        dbSession.save(player);
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

    public CardGlobal getCardGlobalUnique(String cardName, String series) {
        return cardsGlobalManager.getUnique(cardName, series);
    }

    public void removeCard(CardGlobal card) {
        cardsPersonalManager.removeCharacterCards(card.getCharacterInfo());
        cardsGlobalManager.removeCard(card);
    }

    public CardPersonal pickPersonalCard(String playerId, CardGlobal cardGlobal, float pickDelay) {
        cardGlobal = dbSession.load(CardGlobal.class, cardGlobal.getId());
        Player player = dbSession.load(Player.class, playerId);
        cardGlobal.getStats().incrementCardPrint();

        CardPersonal card = getPersonalCardForDelay(cardGlobal, pickDelay);
        savePersonalCard(player, card);

        return card;
    }

    public void savePersonalCard(Player player, CardPersonal card) {
        dbSession.beginTransaction();
        player.addCard(card);
        dbSession.save(card);
        dbSession.getTransaction().commit();
    }

    private CardPersonal getPersonalCardForDelay(CardGlobal card, float delay) {
        return new CardPersonal(card.getCharacterInfo(), card.getStats().getStatsForPickDelay(delay));
    }

    public Paginator getItemShopViewer(User user) {
        return BotMenuCreator.menuShop(eventWaiter, itemsShop, user);
    }

    public Paginator getArmorShopViewer(User user) {
        return BotMenuCreator.menuShop(eventWaiter, armorShop, user);
    }

    public ItemsShop getItemsShop() {
        return itemsShop;
    }

    public ArmorShop getArmorShop() {
        return armorShop;
    }

    @NotNull
    public Squadron getOrCreateSquadron(Player player) {
        Squadron squadron = player.getSquadron();
        return squadron != null ? squadron : createNewSquadron(player);
    }

    @NotNull
    public Squadron createNewSquadron(Player player) {
        Squadron squadron = new Squadron();
        dbSession.beginTransaction();

        if(player.getSquadron() != null){
            squadron.setId(player.getSquadron().getId());
            player.setSquadron(squadron);
            dbSession.merge(squadron);
        }else{
            player.setSquadron(squadron);
            dbSession.save(squadron);
        }

        dbSession.getTransaction().commit();
        return squadron;
    }

    public void createNewPatrol(Player player, PatrolType patrolType, Instant time) {
        if (!player.getSquadron().isEmpty()){
            player.getSquadron().startPatrol(patrolType, time);
            dbSession.merge(player.getSquadron());
        }
    }

    public float exchangeCardForStock(CardPersonal card) {
        StockValue cardStockValue = new StockValue(card);
        dbSession.persist(cardStockValue);
        cardsPersonalManager.removeCard(card);
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

    public CardDropManager getDropManager() {
        return cardDropManager;
    }

    public MaterialsSet finishPatrol(Squadron squadron, Instant time) {
        dbSession.beginTransaction();
        MaterialsSet materials = squadron.finishPatrol(time);
        dbSession.merge(squadron);
        dbSession.getTransaction().commit();
        return materials;
    }

    public void addSquadronMember(Player player, CardPersonal card) {
        dbSession.beginTransaction();
        player.getSquadron().addMember(card);
        dbSession.merge(card);
        dbSession.getTransaction().commit();
    }

    public boolean removeSquadronMembers(Squadron squadron, List<String> memberIds) {
        if(squadron.isEmpty()){
            return false;
        }

        dbSession.beginTransaction();

        List<CardPersonal> newMembers = new ArrayList<>(squadron.getMembers().size());

        // delete all members with matching id
        // else move them to new members list
        long deletedAmount = squadron.getMembers().stream()
                .filter(member -> {
                    int memberIdIndex = memberIds.indexOf(member.getId());
                    if (memberIdIndex != -1) {
                        memberIds.remove(memberIdIndex);
                        return true;
                    } else {
                        newMembers.add(member);
                        return false;
                    }
                })
                .peek(dbSession::delete)
                .count();

        squadron.setMembers(newMembers);
        dbSession.merge(squadron);

        dbSession.getTransaction().commit();

        return deletedAmount > 0;
    }
}

