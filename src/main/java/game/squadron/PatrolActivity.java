package game.squadron;

import game.items.Material;
import game.items.MaterialsSet;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;

@Embeddable
public class PatrolActivity {

    @Enumerated(EnumType.STRING)
    private PatrolType patrolType;
    private Instant started;
    private Instant finished;

    @Transient
    private double patrolTime;


    public PatrolActivity() {
    }

    public PatrolActivity(PatrolType patrolType, double patrolTime) {
        this.patrolType = patrolType;
        this.patrolTime = patrolTime;
    }

    public Instant getStarted() {
        return started;
    }

    public void setStarted(Instant started) {
        this.started = started;
    }

    public PatrolType getPatrolType() {
        return patrolType;
    }

    public MaterialsSet getMaterialsFound(Squadron squadron) {

        long patrolDuration = Duration.between(started, finished).toMillis();
        double percentage = patrolDuration < patrolTime ? patrolDuration / patrolTime : 1.0;

        MaterialsSet materials = new MaterialsSet();
        materials.setAmount(Material.GOLD, (int) Math.round(squadron.getPowerLevel() * percentage));
        return materials;
    }

    public void setFinished(Instant time) {
        finished = time;
    }
}
