package game.player_objects;

import game.Player;
import game.cards.CardPersonal;
import game.cards.SeriesInfo;

import javax.persistence.*;

@Entity
public class StockValue {

    @EmbeddedId
    private StockValueId compositeId;

    @MapsId(value = "ownerId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Player owner;

    @MapsId(value = "seriesId")
    @ManyToOne
    private SeriesInfo series;

    private float value;

    public StockValue() {
    }

    public StockValue(SeriesInfo series, float value) {
        this.series = series;
        this.value = value;
    }

    public StockValue(CardPersonal card) {
        series = card.getCharacterInfo().getSeries();
        value = getCardValue(card);
        owner = card.getOwner();
        owner.getStocks().add(this);
        compositeId = new StockValueId(owner.getId(), series.getId());
    }

    public StockValueId getId() {
        return compositeId;
    }

    public void setId(StockValueId id) {
        this.compositeId = id;
    }

    public void addCardValue(CardPersonal card) {
        value += getCardValue(card);
    }

    public float getCardValue(CardPersonal card) {
        return card.getPowerLevel();
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public SeriesInfo getSeries() {
        return series;
    }

    public void setSeries(SeriesInfo series) {
        this.series = series;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void incrementValue(Float increment) {
        value += increment;
    }

    public String getNameValue() {
        return series.getName() + " - " + value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StockValue && (series.getId() == (((StockValue) obj).getSeries().getId()));
    }
}
