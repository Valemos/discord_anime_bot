package game.cards;

import javax.persistence.*;

@Entity
public class CharacterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = -1;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setSeries(SeriesInfo series) {
        this.series = series;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CharacterInfo){
            return getId() == (((CharacterInfo) obj).getId());
        }
        return false;
    }

    public String getFullName() {
        return name + " / " + series.getName();
    }
}
