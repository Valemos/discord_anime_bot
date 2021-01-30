package game.player_objects.squadron;

import game.materials.Material;
import game.materials.MaterialsSet;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;

@Embeddable
public class Patrol {

    private Instant started;
    private Instant finished;

    @Enumerated(EnumType.STRING)
    private PatrolType patrolType;

    private double patrolTime;


    public Patrol() {
    }

    public Patrol(PatrolType patrolType, double patrolTime) {
        this.patrolType = patrolType;
        this.patrolTime = patrolTime;
    }

    public Instant getStarted() {
        return started;
    }

    public void setStarted(Instant started) {
        this.started = started;
    }

    public void setFinished(Instant time) {
        finished = time;
    }

    public PatrolType getPatrolType() {
        return patrolType;
    }

    public MaterialsSet getMaterialsFound(Squadron squadron) {
        if (!isBusy()){
            return new MaterialsSet();
        }

        long patrolDuration = Duration.between(started, finished).toMillis();
        double percentage = patrolDuration < patrolTime ? patrolDuration / patrolTime : 1.0;

        MaterialsSet materials = new MaterialsSet();
        materials.setAmount(Material.GOLD, (int) Math.round(squadron.getPowerLevel() * percentage));
        return materials;
    }

    public boolean isBusy() {
        return started != null && !isFinished();
    }

    public boolean isFinished() {
        return finished != null;
    }
}
