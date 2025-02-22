package game.cards;

import game.DescriptionDisplayable;

import javax.persistence.*;

@Entity
public class CardGlobal implements DescriptionDisplayable, SearchableCard, ComparableCard {

    @Id
    private String id;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id")
    @MapsId
    CharacterInfo characterInfo;

    @Embedded
    CardStatsGlobal stats;

    public CardGlobal() {
    }

    public CardGlobal(String characterName, String seriesName, String imageUrl) {
        this(characterName, seriesName, imageUrl, new CardStatsGlobal());
    }

    public CardGlobal(String characterName, String seriesName, String imageUrl, CardStatsGlobal stats) {
        characterInfo = new CharacterInfo(characterName, seriesName, imageUrl);
        this.stats = stats;
    }

    public CardStatsGlobal getStats() {
        return stats;
    }

    @Override
    public float getApprovalRating() {
        return stats.getApprovalRating();
    }

    @Override
    public int getPrint() {
        return stats.amountCardsPrinted;
    }

    public CharacterInfo getCharacterInfo() {
        return characterInfo;
    }

    public void setCharacterInfo(CharacterInfo characterInfo) {
        this.characterInfo = characterInfo;
    }

    @Override
    public String getName() {
        return characterInfo.getName();
    }

    @Override
    public String getSeriesName() {
        return characterInfo.getSeriesName();
    }

    public void setCardInfo(CardGlobal card) {
        id = card.id;
        characterInfo = card.characterInfo;
        stats = card.stats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getStatsString() {
        return stats.getDescription();
    }

    @Override
    public String getFullDescription() {
        return getIdNameStats();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CardGlobal){
            return id.equals(((CardGlobal) obj).id);
        }
        return false;
    }
}
