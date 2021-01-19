package game;

import game.cards.CardPersonal;
import game.cards.SeriesInfo;
import game.items.MaterialsSet;
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
