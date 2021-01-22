package game.cards;

import bot.ShortUUID;
import game.DescriptionDisplayable;
import game.Player;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class CardPersonal implements DescriptionDisplayable, SearchableCard, ComparableCard {

    @Id
    private String id;

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
        id = ShortUUID.generate();
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
    public String getSeriesName() {
        return characterInfo.getSeriesName();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return characterInfo.getName();
    }

    @Override
    public String getFullName() {
        return characterInfo.getFullName();
    }

    @Override
    public String getStatsString() {
        return stats.getDescription();
    }
}
