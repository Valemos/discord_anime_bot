package game.cards;

import bot.ShortUUID;
import game.DescriptionDisplayable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class CardGlobal implements DescriptionDisplayable, SearchableCard, ComparableCard {

    @Id
    private String id;

    @Embedded
    CardStatsGlobal stats;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    CharacterInfo characterInfo;

    public CardGlobal() {
    }

    public CardGlobal(String characterName, String seriesName, String imageUrl) {
        this(characterName, seriesName, imageUrl, new CardStatsGlobal());
    }

    public CardGlobal(String characterName, String seriesName, String imageUrl, CardStatsGlobal stats) {
        characterInfo = new CharacterInfo(characterName, seriesName, imageUrl);
        this.stats = stats;
    }

    @PrePersist
    public void generateId() {
        id = ShortUUID.generate();
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

    @Override
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
        return stats.getStatsForPickDelay(0).getDescription();
    }

    @Override
    public String getIdNameStats() {
        return getIdName() + "\n" + getStatsString();
    }

    @Override
    public String getFullDescription() {
        return getIdNameStats();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CardGlobal){
            return id.equals(((CardGlobal) obj).getId());
        }
        return false;
    }
}
