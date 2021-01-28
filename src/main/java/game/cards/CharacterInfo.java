package game.cards;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "series_id"})})
public class CharacterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base36_card_global")
    @GenericGenerator(name = "base36_card_global", strategy = "bot.Base36SequenceGenerator")
    private String id;

    private String name;

    private String imageUrl;

    @OneToOne
    private SeriesInfo series;

    public CharacterInfo() {
    }

    public CharacterInfo(String name, String seriesName, String imageUrl) {
        this.name = name;
        this.series = new SeriesInfo(seriesName);
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SeriesInfo getSeries() {
        return series;
    }

    public void setSeries(SeriesInfo series) {
        this.series = series;
    }

    public String getSeriesName() {
        return series.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CharacterInfo){
            return getId().equals(((CharacterInfo) obj).getId());
        }
        return false;
    }

    public String getFullName() {
        return name + " / " + series.getName();
    }
}
