package game;

import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.cards.*;
import game.contract.ContractsManager;
import game.materials.Material;
import game.materials.MaterialsSet;
import game.player_objects.StockValue;
import game.player_objects.StockValueId;
import game.player_objects.squadron.PatrolType;
import game.player_objects.squadron.Squadron;
import game.shop.ArmorShop;
import game.shop.ItemsShop;
import game.shop.items.ArmorItem;
import net.dv8tion.jda.api.entities.User;
import org.hibernate.Session;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnimeCardsGame {
    private final EventWaiter eventWaiter;
    private final Session dbSession;

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
        List<ArmorItem> armorItems = updateArmorItems(dbSession);
        armorShop = new ArmorShop(armorItems);
        cardDropManager = new CardDropManager(this, dbSession);
        contractsManager = new ContractsManager();
    }

    public void reset() {
        setSession(dbSession);
    }

    private List<ArmorItem> updateArmorItems(Session dbSession) {
        List<ArmorItem> armorItems = getArmorItems();

        for(ArmorItem armor : armorItems){
            if (armor.getId() == null){
                ArmorItem existing = findArmorByName(dbSession, armor.getName());

                if (existing != null){
                    armor.setId(existing.getId());
                    dbSession.merge(armor);
                }else{
                    dbSession.beginTransaction();
                    dbSession.save(armor);
                    dbSession.getTransaction().commit();
                }
            }else{
                dbSession.merge(armor);
            }
        }

        // TODO write armor ids to json file to update them in future bot instantiation
        return armorItems;
    }

    private ArmorItem findArmorByName(Session dbSession, String name) {
        CriteriaBuilder cb = dbSession.getCriteriaBuilder();
        CriteriaQuery<ArmorItem> q = cb.createQuery(ArmorItem.class);
        Root<ArmorItem> from = q.from(ArmorItem.class);

        return dbSession.createQuery(
                q.select(from).where(cb.equal(cb.lower(from.get("name")), name.toLowerCase()))
        ).uniqueResult();
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

    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    @Nonnull
    public Player getOrCreatePlayer(String playerId) {
        Player player = getPlayer(playerId);
        return player != null ? player : createNewPlayer(playerId);
    }

    @Nullable
    public Player getPlayer(String playerId) {
        return dbSession.get(Player.class, playerId);
    }

    public Player createNewPlayer(String playerId) {
        Player player = new Player();
        player.setId(playerId);
        dbSession.beginTransaction();
        dbSession.save(player);
        dbSession.getTransaction().commit();
        return player;
    }

    public CardsPersonalManager getCardsPersonal() {
        return cardsPersonalManager;
    }

    public boolean addCard(CardGlobal card) {
        return cardsGlobalManager.addCard(card);
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

    public CardPersonal pickPersonalCard(String playerId, CardGlobal cardGlobal, CardPickInfo pickInfo) {
        cardGlobal = dbSession.load(CardGlobal.class, cardGlobal.getId());
        Player player = dbSession.load(Player.class, playerId);
        cardGlobal.getStats().incrementCardPrint();

        CardPersonal card = getPersonalCard(cardGlobal, pickInfo);
        savePersonalCard(player, card);

        return card;
    }

    public void savePersonalCard(Player player, CardPersonal card) {
        dbSession.beginTransaction();
        player.addCard(card);
        dbSession.save(card);
        dbSession.getTransaction().commit();
    }

    private CardPersonal getPersonalCard(CardGlobal card, CardPickInfo pickInfo) {
        return new CardPersonal(card.getCharacterInfo(), card.getStats().getConstantStats(pickInfo));
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

    @Nonnull
    public Squadron getOrCreateSquadron(Player player) {
        Squadron squadron = player.getSquadron();
        return squadron != null ? squadron : createNewSquadron(player);
    }

    @Nonnull
    public Squadron createNewSquadron(Player player) {
        Squadron squadron = new Squadron();
        dbSession.beginTransaction();
        player.setSquadron(squadron);
        dbSession.save(squadron);
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
            StockValue cardStock = dbSession.find(
                    StockValue.class,
                    new StockValueId(card.getOwner().getId(), card.getCharacterInfo().getSeries().getId())
            );

            if (cardStock != null){
                cardStock.addCardValue(card);
                dbSession.merge(cardStock);
            }else{
                cardStock = new StockValue(card);
                dbSession.persist(cardStock);
                dbSession.merge(cardStock.getOwner());
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
        cardsGlobalManager.update(cardGlobal);
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
        Squadron squadron = getOrCreateSquadron(player);

        dbSession.beginTransaction();
        squadron.addMember(card);
        dbSession.merge(squadron);
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
                .peek(member -> member.setAssignedSquadron(null))
                .count();

        squadron.setMembers(newMembers);
        dbSession.merge(squadron);

        dbSession.getTransaction().commit();

        return deletedAmount > 0;
    }

    public void clearSquadron(Squadron squadron) {
        dbSession.beginTransaction();

        for(CardPersonal member : squadron.getMembers()){
            member.setAssignedSquadron(null);
            dbSession.merge(member);
        }

        squadron.clearMembers();
        dbSession.merge(squadron);

        dbSession.getTransaction().commit();
    }

    public void removeArmorItemsPersonal(ArmorItem item) {
        dbSession.beginTransaction();
        dbSession.createQuery("delete from ArmorItemPersonal as a where a.original.id = :itemId")
                .setParameter("itemId", item.getId())
                .executeUpdate();
        dbSession.getTransaction().commit();
    }

    public void removeStocks(Player player) {
        dbSession.beginTransaction();
        for (StockValue stock : player.getStocks()){
            dbSession.delete(stock);
        }
        player.getStocks().clear();
        dbSession.merge(player);
        dbSession.getTransaction().commit();
    }

    public void clearSquadronsTable() {
        List<Squadron> squadrons = getAllObjects(dbSession, Squadron.class);
        for (Squadron sq : squadrons){
            clearSquadron(sq);

            dbSession.beginTransaction();
            sq.getOwner().setSquadron(null);
            dbSession.merge(sq.getOwner());
            dbSession.delete(sq);
            dbSession.getTransaction().commit();
        }
    }

    public static <T> List<T> getAllObjects(Session s, Class<T> entityClass) {
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(entityClass);
        return s.createQuery(q.select(q.from(entityClass))).list();
    }
}

