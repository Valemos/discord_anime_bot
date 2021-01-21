package game.shop.items;

import game.Player;
import game.materials.Material;
import game.materials.MaterialsSet;
import game.squadron.SquadronPowerUp;
import game.squadron.Squadron;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Map;

@Entity
public abstract class ItemPowerUp extends AbstractShopItem {

    public ItemPowerUp() {
        super(null, new MaterialsSet());
    }

    public ItemPowerUp(String name, Map<Material, Integer> materials) {
        super(name, materials);
    }

    @Override
    public void useFor(Player player) {
        if (player.getSquadron() != null){
            player.getSquadron().getPowerUps().add(new SquadronPowerUp(player.getSquadron(), this));
        }
    }

    public abstract float getAdditionalPower(Squadron owner);

    @Id
    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }
}
