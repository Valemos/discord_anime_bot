package game;

import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.materials.MaterialsSet;
import game.player_objects.CooldownSet;
import game.player_objects.squadron.Squadron;
import game.player_objects.StockValue;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany
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

    public StockValue findStockByName(String seriesName) {
        return getStocks().stream()
                .filter((s) -> String.valueOf(s.getSeries().getName()).toLowerCase()
                        .contains(seriesName.toLowerCase()))
                .findFirst().orElse(null);
    }


    public Squadron getSquadron() {
        return squadron;
    }

    public void setSquadron(Squadron squadron) {
        squadron.setOwner(this);
        this.squadron = squadron;
    }


    public CooldownSet getCooldowns() {
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
}
