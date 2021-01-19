package game.cards;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class CharacterInfo {

    @Id
    private String id; //TODO add id generation
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

    public String getName() {
        return name;
    }

    public String getSeriesName() {
        return series.getName();
    }

    public SeriesInfo getSeries() {
        return series;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CharacterInfo){
            return getId().equals(((CharacterInfo) obj).getId());
        }
        return false;
    }

    public String getFullName() {
        return name + " / " + series;
    }
}
