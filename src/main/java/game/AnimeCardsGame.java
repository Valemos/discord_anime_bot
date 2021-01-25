package game;

import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.cards.*;
import game.contract.ContractsManager;
import game.materials.*;
import game.shop.ArmorShop;
import game.shop.ItemsShop;
import game.shop.items.ArmorItem;
import game.player_objects.squadron.PatrolType;
import game.player_objects.squadron.Squadron;
import game.player_objects.StockValue;
import game.player_objects.StockValueId;
import net.dv8tion.jda.api.entities.User;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.*;

public class AnimeCardsGame {
    private final EventWaiter eventWaiter;
    private Session dbSession;

    private ItemsShop itemsShop;
    private ArmorShop armorShop;

    private CardsGlobalManager cardsGlobalManager;
    private CardsPersonalManager cardsPersonalManager;

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

        itemsShop = new ItemsShop();

        List<ArmorItem> armorItems = saveArmorItems(dbSession);
        armorShop = new ArmorShop(armorItems);


        cardDropManager = new CardDropManager(this, dbSession);
        contractsManager = new ContractsManager();
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
        if (burnCard(card)){
            dbSession.beginTransaction();
            StockValue cardStock = dbSession.get(StockValue.class, new StockValueId(
                                                        card.getOwner().getId(),
                                                        card.getCharacterInfo().getSeries().getId()));
            if (cardStock != null){
                cardStock.addCardValue(card);
                dbSession.merge(cardStock);
            }else{
                cardStock = new StockValue(card);
                dbSession.save(cardStock);
            }
            dbSession.getTransaction().commit();

            return cardStock.getValue();
        }
        return 0;
    }

    private boolean burnCard(CardPersonal card) {
        CardGlobal cardGlobal = cardsGlobalManager.getByCharacter(card.getCharacterInfo());
        if (cardGlobal == null) return false;

        cardGlobal.getStats().incrementCardBurned();
        cardsPersonalManager.removeCard(card);
        return true;
    }

    public void addToWishlist(Player player, CardGlobal card) {
        dbSession.beginTransaction();
        dbSession.persist(player);
        player.getWishList().add(card);
        dbSession.getTransaction().commit();
    }

    public boolean removeFromWishlist(Player player, String cardId) {
        dbSession.beginTransaction();

        dbSession.persist(player);
        boolean isElementRemoved = player.getWishList().removeIf(card -> card.getId().equals(cardId));

        dbSession.getTransaction().commit();
        return isElementRemoved;
    }

    public boolean removeFromWishlist(Player player, CardGlobal card) {
        dbSession.beginTransaction();

        dbSession.persist(player);
        boolean isElementRemoved = player.getWishList().remove(card);

        dbSession.getTransaction().commit();
        return isElementRemoved;
    }

    public ContractsManager getContractsManager() {
        return contractsManager;
    }

    public Player getCardPersonalOwner(String cardId) {
        CardPersonal card = cardsPersonalManager.getById(cardId);
        return card != null ? card.getOwner() : null;
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

        Set<CardPersonal> newMembers = new HashSet<>(squadron.getMembers().size());

        // delete all members with matching id
        // else move them to new members list
        long deletedAmount = squadron.getMembers().stream()
                .filter(member -> {
                    int memberIdIndex = memberIds.indexOf(member.getId());
                    if (memberIdIndex != -1) { // member found, delete
                        memberIds.remove(memberIdIndex);
                        return true;
                    } else { // member not found, save to new members list
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

