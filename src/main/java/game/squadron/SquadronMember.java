package game.squadron;

import game.cards.CardPersonal;
import game.cards.CardStats;
import game.cards.ComparableCard;

import javax.persistence.*;

@Entity
public class SquadronMember implements ComparableCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.ORDINAL)
    HealthState healthState;

    @OneToOne
    CardPersonal card;

    public SquadronMember() {
    }

    public SquadronMember(CardPersonal card) {
        this.card = card;
        healthState = HealthState.HEALTHY;
    }

    public SquadronMember(CardPersonal card, HealthState healthState) {
        this.healthState = healthState;
        this.card = card;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HealthState getHealthState() {
        return healthState;
    }

    public void setHealthState(HealthState healthState) {
        this.healthState = healthState;
    }

    public CardPersonal getCard() {
        return card;
    }

    public void setCard(CardPersonal card) {
        this.card = card;
    }

    @Override
    public CardStats getStats() {
        return card.getStats();
    }

    @Override
    public float getApprovalRating() {
        return card.getApprovalRating();
    }

    @Override
    public int getPrint() {
        return card.getPrint();
    }

    public String getDescription() {
        return getCard().getNameStats() + " / " + getHealthState().name().toLowerCase();
    }
}
