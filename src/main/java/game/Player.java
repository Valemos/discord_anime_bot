package game;

import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.cards.SeriesInfo;
import game.materials.MaterialsSet;
import game.player_objects.ArmorItemPersonal;
import game.player_objects.CooldownSet;
import game.player_objects.squadron.Squadron;
import game.player_objects.StockValue;
import game.shop.items.AbstractShopItem;
import game.shop.items.UsablePowerUp;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Player {

    @Id
    private String id;

    @OneToMany(mappedBy="owner")
    private final List<CardPersonal> cards = new ArrayList<>();

    @Embedded
    private final MaterialsSet materials = new MaterialsSet();

    @OneToMany(mappedBy="owner")
    private List<StockValue> stocks = new ArrayList<>();

    @OneToOne
    private Squadron squadron;

    @ElementCollection
    @MapKeyColumn(name = "poweruptype")
    private Map<Class<? extends UsablePowerUp>, Integer> powerUps = new HashMap<>();

    @OneToMany(mappedBy="owner")
    private final List<ArmorItemPersonal> armorItems = new ArrayList<>();

    @Embedded
    private CooldownSet cooldowns = new CooldownSet();

    @ManyToMany
    private Set<CardGlobal> wishList = new HashSet<>();

    public Player() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player){
            return id.equals(((Player) obj).id);
        }
        return false;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public MaterialsSet getMaterials() {
        return materials;
    }

    public void subtractMaterials(MaterialsSet materials) {
        this.materials.subtractMaterials(materials);
    }

    public void addMaterials(MaterialsSet materials) {
        this.materials.addMaterials(materials);
    }


    public void addCard(CardPersonal card) {
        card.setOwner(this);
        cards.add(card);
    }

    public List<CardPersonal> getCards() {
        return cards;
    }

    public Set<CardGlobal> getWishList() {
        return wishList;
    }

    public void setWishList(Set<CardGlobal> wishList) {
        this.wishList = wishList;
    }

    public List<StockValue> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockValue> stocks) {
        this.stocks = stocks;
    }

    public Map<Class<? extends UsablePowerUp>, Integer> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(Map<Class<? extends UsablePowerUp>, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    public void addPowerUp(Class<? extends UsablePowerUp> powerUp) {
        powerUps.put(powerUp, 1 + powerUps.getOrDefault(powerUp, 0));
    }

    private StockValue findStockBySeries(SeriesInfo series) {
        return getStocks().stream()
                .filter(s -> s.getSeries().equals(series))
                .findFirst().orElse(null);
    }

    public StockValue findStockByUniqueName(String seriesName) {
        List<StockValue> stocks = getStocks().stream()
                                    .filter((s) -> String.valueOf(s.getSeries().getName()).toLowerCase()
                                            .contains(seriesName.toLowerCase()))
                                    .collect(Collectors.toList());

        return stocks.size() == 1 ? stocks.get(0) : null;
    }

    public Squadron getSquadron() {
        return squadron;
    }

    public void setSquadron(Squadron squadron) {
        if (squadron != null) squadron.setOwner(this);
        this.squadron = squadron;
    }

    @Nonnull
    public CooldownSet getCooldowns() {
        if (cooldowns == null) cooldowns = new CooldownSet();
        return cooldowns;
    }

    public void setCooldowns(CooldownSet cooldowns) {
        this.cooldowns = cooldowns;
    }


    public void addArmorItem(ArmorItemPersonal armorItem) {
        this.armorItems.add(armorItem);
    }

    public List<ArmorItemPersonal> getArmorItems() {
        return armorItems;
    }

    public void addStocks(Map<SeriesInfo, Float> stocks) {
        incrementStocks(stocks, 1);
    }

    public void removeStocks(Map<SeriesInfo, Float> stocks) {
        incrementStocks(stocks, -1);
    }

    private void incrementStocks(Map<SeriesInfo, Float> stocks, int sign) {
        stocks.keySet().stream()
                .peek(seriesInfo -> getStocks().stream()
                        .filter(stock -> stock.getSeries().equals(seriesInfo))
                        .findFirst()
                        .ifPresent(playerStock -> playerStock.incrementValue(sign * stocks.get(seriesInfo)))
                ).close();
    }

    public Map<SeriesInfo, Float> getAvailableStocks(Map<SeriesInfo, Float> stockValues) {
        Map<SeriesInfo, Float> newValues = new HashMap<>();
        stockValues.forEach((series, value) -> {
            StockValue stock = findStockBySeries(series);
            newValues.put(series, stock.getValue() > value ? value : stock.getValue());
        });
        return newValues;
    }

    public MaterialsSet getAvailableMaterials(MaterialsSet materialsInput) {
        MaterialsSet newValues = new MaterialsSet();
        materialsInput.getMap().forEach((material, value) -> {
            int availableAmount = materials.getAmount(material);
            newValues.setAmount(material, availableAmount > value ? value : availableAmount);
        });
        return newValues;
    }

    public void usePowerUp(AnimeCardsGame game, UsablePowerUp powerUp) {
        powerUp.useFor(game, this);
        powerUps.put(powerUp.getClass(), powerUps.get(powerUp.getClass()) - 1);
    }

    public boolean canUseItem(UsablePowerUp powerUp) {
        Class<? extends UsablePowerUp> powerUpClass = powerUp.getClass();
        int powerUpAmount = powerUps.getOrDefault(powerUpClass, 0);

        if (powerUpAmount == 0) {
            powerUps.remove(powerUpClass);
            return false;
        }else return true;
    }
}
