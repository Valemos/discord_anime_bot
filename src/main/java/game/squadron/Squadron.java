package game.squadron;

import game.Player;
import game.cards.CardPersonal;
import game.cards.ComparableCard;
import game.items.MaterialsSet;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.min;

@Entity
public class Squadron {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "squadronId")
    private long id;

    @OneToOne
    private Player owner;

    @Embedded
    private PatrolActivity patrol;

    @Transient
    private final int sizeMax = 4;

    @OneToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "squadronId", nullable = false)
    List<SquadronMember> squadronMembers = new ArrayList<>();

    public Squadron() {
    }

    public long getId() {
        return id;
    }

    public Player getOwner() {
        return owner;
    }

    public List<SquadronMember> getSortedMembers() {
        return squadronMembers.stream()
                .sorted(ComparableCard::comparatorPower)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return squadronMembers.isEmpty();
    }

    public void addCard(CardPersonal card) {
        squadronMembers.add(new SquadronMember(card));
    }

    public boolean isFull() {
        return squadronMembers.size() >= sizeMax;
    }

    public List<SquadronMember> getMembers() {
        return squadronMembers;
    }

    public float getPowerLevel() {
        float totalPowerLevel = 0;
        for (SquadronMember member : getMembers()){
            float cardPowerLevel = member.getStats().getPowerLevel();
            HealthState cardHealth = member.getHealthState();
            totalPowerLevel += cardHealth == HealthState.INJURED ? cardPowerLevel * 0.2 : cardPowerLevel;
        }
        return totalPowerLevel;
    }

    public void startPatrol(PatrolType patrolType, Instant time) {
        final int hourMillis = 60 * 60 * 1000;
        patrol = new PatrolActivity(patrolType, hourMillis);
        patrol.setStarted(time);
    }

    public MaterialsSet finishPatrol(Instant time) {
        patrol.setFinished(time);
        MaterialsSet materials = patrol.getMaterialsFound(this);
        owner.addMaterials(materials);

        return materials;
    }

}
