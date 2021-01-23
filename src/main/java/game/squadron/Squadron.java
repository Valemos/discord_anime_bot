package game.squadron;

import game.Player;
import game.cards.CardPersonal;
import game.cards.ComparableCard;
import game.materials.MaterialsSet;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Squadron {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(mappedBy = "squadron")
    private Player owner;

    @Embedded
    private Patrol patrol = new Patrol();

    @ElementCollection
    private List<PowerUpType> powerUps = new ArrayList<>();

    @Transient
    public static final int sizeMax = 4;

    @OneToMany(mappedBy = "assignedSquadron")
    Set<CardPersonal> squadronMembers = new HashSet<>();

    public Squadron() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Patrol getPatrol() {
        return patrol;
    }

    public void setPatrol(Patrol patrol) {
        this.patrol = patrol;
    }

    public List<CardPersonal> getSortedMembers() {
        return squadronMembers.stream()
                .sorted(ComparableCard::comparatorPower)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return squadronMembers.isEmpty();
    }

    public void addMember(CardPersonal member) {
        member.setAssignedSquadron(this);
        squadronMembers.add(member);
    }

    public boolean isFull() {
        return squadronMembers.size() >= sizeMax;
    }

    public Set<CardPersonal> getMembers() {
        return squadronMembers;
    }

    public void setMembers(Set<CardPersonal> squadronMembers) {
        this.squadronMembers = squadronMembers;
    }

    public List<PowerUpType> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(List<PowerUpType> powerUps) {
        this.powerUps = powerUps;
    }

    public void addPowerUp(PowerUpType powerUp) {
        powerUps.add(powerUp);
    }

    public String getPowerUpsDescription(){
        return powerUps.stream().map(PowerUpType::getName).collect(Collectors.joining("\n"));
    }

    public float getPowerLevel() {
        float totalPowerLevel = 0;
        for (CardPersonal member : getMembers()){
            float cardPowerLevel = member.getStats().getPowerLevel();
            HealthState cardHealth = member.getHealthState();
            totalPowerLevel += cardHealth == HealthState.INJURED ? cardPowerLevel * 0.2 : cardPowerLevel;
        }

        for (PowerUpType powerUp : powerUps){
            totalPowerLevel += getAdditionalPower(powerUp, this);
        }

        return totalPowerLevel;
    }

    private float getAdditionalPower(PowerUpType powerUp, Squadron squadron) {
        return 100; // TODO add power up logic
    }

    public void startPatrol(PatrolType patrolType, Instant time) {
        patrol = new Patrol(patrolType, 60 * 60 * 1000);
        patrol.setStarted(time);
    }

    public MaterialsSet finishPatrol(Instant time) {
        patrol.setFinished(time);

        MaterialsSet materials = patrol.getMaterialsFound(this);
        owner.addMaterials(materials);

        return materials;
    }

    public void healMembers() {
        for (CardPersonal member : squadronMembers){
            member.setHealthState(HealthState.HEALTHY);
        }
    }

    public String getDescription() {
        return getSortedMembers().stream()
                .map(c -> c.getIdName() + " ("+c.getHealthState().toString()+')')
                .collect(Collectors.joining("\n"))
                + "\nPower ups:\n"
                + getPowerUpsDescription();
    }
}
