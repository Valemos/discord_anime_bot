package game.cards;

import game.DisplayableStats;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class CardGlobal implements DisplayableStats, ComparableCard {

    @Id
    private String id; //TODO add generate id
    @OneToOne
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
    public CharacterInfo getCharacterInfo() {
        return characterInfo;
    }

    @Override
    public String getName() {
        return characterInfo.getName();
    }

    @Override
    public String getSeries() {
        return characterInfo.getSeriesName();
    }

    public String getId() {
        return characterInfo.getId();
    }

    public String getFullName() {
        return characterInfo.getFullName();
    }

    @Override
    public String getNameStats() {
        return characterInfo.getFullName()
                + ": "
                + stats.getStatsForPickDelay(0).getDescription();
    }

    @Override
    public String getIdName() {
        return getId() + " - " + getFullName();
    }

    @Override
    public String getIdNameStats() {
        return getIdName() + "\n" + stats.getStatsForPickDelay(0).getDescription();
    }

    @Override
    public String getFullDescription() {
        return getIdNameStats();
    }

    public static int comparatorStats(CardGlobal card1, CardGlobal card2) {
        return card2.getStats().compareTo(card1.getStats());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CardGlobal){
            return characterInfo.equals(((CardGlobal) obj).getCharacterInfo());
        }
        return false;
    }
}
