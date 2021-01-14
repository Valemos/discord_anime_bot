package game.squadron;

import game.items.Material;
import game.items.MaterialsSet;

public class PatrolActivity {
    private final Squadron squadron;
    private final PatrolType patrolType;
    private boolean isFinished;

    public PatrolActivity(Squadron squadron, PatrolType patrolType) {
        this.squadron = squadron;
        this.patrolType = patrolType;
    }

    public Squadron getSquadron() {
        return squadron;
    }

    public PatrolType getPatrolType() {
        return patrolType;
    }

    public MaterialsSet getMaterialsFound() {
        if (isFinished){
            return new MaterialsSet();
        }

        MaterialsSet materials = new MaterialsSet();
        materials.setAmount(Material.GOLD, Math.round(squadron.getPowerLevel()));
        return materials;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }
}
