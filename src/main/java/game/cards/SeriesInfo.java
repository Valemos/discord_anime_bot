package game.cards;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SeriesInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;

    public SeriesInfo() {
    }

    public SeriesInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
