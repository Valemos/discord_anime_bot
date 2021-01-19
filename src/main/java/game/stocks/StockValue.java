package game.stocks;

import game.Player;
import game.cards.CardPersonal;
import game.cards.SeriesInfo;

import javax.persistence.*;

@Entity
public class StockValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    private Player owner;

    @OneToOne
    private SeriesInfo series;

    private float value;

    public StockValue() {
    }

    public StockValue(CardPersonal card) {
        series = card.getCharacterInfo().getSeries();
        owner = card.getOwner();
        value = getCardValue(card);
    }

    public float getCardValue(CardPersonal card) {
        return card.getPowerLevel();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
