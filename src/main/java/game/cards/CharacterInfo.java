package game.cards;

public class CharacterInfo {

    int id;
    String characterName;
    String seriesName;
    String imageUrl;

    public CharacterInfo(String characterName, String seriesName, String imageUrl) {
        this.characterName = characterName;
        this.seriesName = seriesName;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CharacterInfo){
            return this.id == ((CharacterInfo) obj).id;
        }
        return false;
    }
}
