package game.squadron;

import game.shop.items.ItemPowerUp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SquadronPowerUp {

    @Id
    private int id;

    @ManyToOne
    private Squadron owner;

    @ManyToOne
    private ItemPowerUp original;

    public SquadronPowerUp() {
    }

    public SquadronPowerUp(Squadron owner, ItemPowerUp original) {
        this.owner = owner;
        this.original = original;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Squadron getOwner() {
        return owner;
    }

    public void setOwner(Squadron owner) {
        this.owner = owner;
    }

    public float getAdditionalPower() {
        return original.getAdditionalPower(owner);
    }

    public String getName() {
        return original.getName();
    }
}
