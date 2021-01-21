package game;

import game.cards.CardPersonal;
import game.cards.SeriesInfo;
import game.cooldown.CooldownSet;
import game.items.MaterialsSet;
import game.squadron.Squadron;
import game.stocks.StockValue;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class Player {

    @Id
    private String id;

    @OneToMany(mappedBy="owner")
    private final List<CardPersonal> cards = new ArrayList<>();

    @Embedded
    private final MaterialsSet materials = new MaterialsSet();

    @OneToMany(mappedBy="owner")
    private List<StockValue> stocks;

    @OneToOne
    private Squadron squadron;

    @Embedded
    private CooldownSet cooldowns = new CooldownSet();

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

    public void addCard(CardPersonal card) {
        card.setOwner(this);
        cards.add(card);
    }

    public List<CardPersonal> getCards() {
        return cards;
    }

    public List<StockValue> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockValue> stocks) {
        this.stocks = stocks;
    }

    public Squadron getSquadron() {
        return squadron;
    }

    public void setSquadron(Squadron squadron) {
        this.squadron = squadron;
    }

    public CooldownSet getCooldowns() {
        return cooldowns;
    }

    public void setCooldowns(CooldownSet cooldowns) {
        this.cooldowns = cooldowns;
    }

    public void subtractMaterials(MaterialsSet materials) {
        this.materials.subtractMaterials(materials);
    }

    public void addMaterials(MaterialsSet materials) {
        this.materials.addMaterials(materials);
    }

    public StockValue findStockByName(String seriesName) {
        return getStocks().stream()
                .filter((s) -> String.valueOf(s.getSeries().getName()).toLowerCase()
                        .contains(seriesName.toLowerCase()))
                .findFirst().orElse(null);
    }
}
