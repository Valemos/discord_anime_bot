package game.stocks;

import game.Player;
import game.cards.CardPersonal;
import game.cards.SeriesInfo;

import javax.persistence.*;

@Entity
@IdClass(StockValueId.class)
public class StockValue {

    @Id
    @ManyToOne
    private Player owner;

    @Id
    @OneToOne
    private SeriesInfo series;

    private float value;

    public StockValue() {
    }

    public StockValue(CardPersonal card) {
        series = card.getCharacterInfo().getSeries();
        owner = card.getOwner();
        owner.getStocks().add(this);
        value = getCardValue(card);
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

    public String getNameValue() {
        return series.getName() + " - " + value;
    }
}
