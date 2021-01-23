package game.player_objects;

import java.io.Serializable;

public class StockValueId implements Serializable {
    String owner;
    int series;

    public StockValueId() {
    }

    public StockValueId(String owner, int series) {
        this.owner = owner;
        this.series = series;
    }
}
