package game.player_objects;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StockValueId implements Serializable {
    String ownerId;
    int seriesId;

    public StockValueId() {
    }

    public StockValueId(String ownerId, int seriesId) {
        this.ownerId = ownerId;
        this.seriesId = seriesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockValueId that = (StockValueId) o;
        return seriesId == that.seriesId && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, seriesId);
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String owner_id) {
        this.ownerId = owner_id;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int series_id) {
        this.seriesId = series_id;
    }
}
