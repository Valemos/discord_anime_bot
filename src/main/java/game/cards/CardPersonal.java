package game.cards;

import game.DisplayableStats;
import game.Player;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

@Entity
public class CardPersonal implements DisplayableStats, ComparableCard {

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
        String uuid_string = UUID.randomUUID().toString().replaceAll("-","");
        BigInteger big = new BigInteger(uuid_string, 16);
        cardId = big.toString(36);
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

    public float getApprovalRating(){
        return stats.approvalRating;
    }

    public int getCardPrint() {
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

    public static int comparatorPowerLevel(CardPersonal card1, CardPersonal card2) {
        return Float.compare(card2.getPowerLevel(), card1.getPowerLevel());
    }
}
