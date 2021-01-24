package game.cards;

import bot.Base36SequenceGenerator;
import game.DescriptionDisplayable;
import game.Player;
import game.player_objects.squadron.HealthState;
import game.player_objects.squadron.Squadron;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class CardPersonal implements DescriptionDisplayable, SearchableCard, ComparableCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base36_card_personal")
    @GenericGenerator(name = "base36_card_personal", strategy = "bot.Base36SequenceGenerator")
    private String id;

    @ManyToOne
    private Player owner;

    @ManyToOne
    private CharacterInfo characterInfo;

    @Embedded
    private CardStatsConstant stats;

    @ManyToOne
    private Squadron assignedSquadron;

    @Enumerated(EnumType.ORDINAL)
    private HealthState healthState = HealthState.HEALTHY;


    public CardPersonal() {
    }

    public CardPersonal(CharacterInfo characterInfo, CardStatsConstant stats) {
        this.characterInfo = characterInfo;
        this.stats = stats;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

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

    @Override
    public int getPrint() {
        return stats.cardPrint;
    }

    @Override
    public float getApprovalRating(){
        return stats.approvalRating;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCharacterInfo(CharacterInfo characterInfo) {
        this.characterInfo = characterInfo;
    }

    public Squadron getAssignedSquadron() {
        return assignedSquadron;
    }

    public void setAssignedSquadron(Squadron squadron) {
        this.assignedSquadron = squadron;
    }

    public HealthState getHealthState() {
        return healthState;
    }

    public void setHealthState(HealthState healthState) {
        this.healthState = healthState;
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
