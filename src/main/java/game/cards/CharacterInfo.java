package game.cards;

public class CharacterInfo {

    String id;
    String characterName;
    String seriesTitle;
    String imageUrl;

    public CharacterInfo(String characterName, String seriesTitle, String imageUrl) {
        this.characterName = characterName;
        this.seriesTitle = seriesTitle;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CharacterInfo){
            return this.id.equals(((CharacterInfo) obj).id);
        }
        return false;
    }

    public String getFullName() {
        return characterName + " / " + seriesTitle;
    }
}
