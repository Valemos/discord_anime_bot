package game.cards;

import game.DisplayableStats;
import game.Player;

import javax.persistence.*;
import java.util.Objects;
import java.util.stream.Stream;

@Entity
public class CardPersonal implements DisplayableStats, DisplayableCard, ComparableCard {

    @Id
    private String cardId;

    @ManyToOne
    private Player owner;

    @ManyToOne
    CharacterInfo characterInfo;

    @Embedded
    CardStatsConstant stats;

    public CardPersonal() {
    }

    public CardPersonal(CharacterInfo characterInfo, CardStatsConstant stats) {
        this.characterInfo = characterInfo;
        this.stats = stats;
    }

    @PrePersist
    public void generateId() {
        cardId = ShortUUID.generate();
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public CharacterInfo getCharacterInfo() {
        return characterInfo;
    }

    public void setStats(CardStatsConstant stats) {
        this.stats = stats;
    }

    @Override
    public CardStats getStats() {
        return stats;
    }

    public float getApprovalRating(){
        return stats.approvalRating;
    }

    @Override
    public int getPrint() {
        return stats.cardPrint;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardPersonal){
            CardPersonal other = (CardPersonal) obj;
            return  Objects.equals(characterInfo, other.characterInfo) &&
                    Objects.equals(stats, other.stats);
        }
        return false;
    }

    public float getPowerLevel(){
        return stats.getPowerLevel();
    }

    @Override
    public String getName() {
        return characterInfo.getName();
    }

    @Override
    public String getSeries() {
        return characterInfo.getSeriesName();
    }

    @Override
    public String getId() {
        return cardId;
    }

    @Override
    public String getNameStats() {
        return characterInfo.getFullName() + ": " + stats.getDescription();
    }

    @Override
    public String getIdName() {
        return cardId + " - " + characterInfo.getFullName();
    }

    @Override
    public String getIdNameStats() {
        return getIdName() + ": " + stats.getDescription();
    }

    @Override
    public String getFullDescription() {
        return getIdNameStats();
    }

}
