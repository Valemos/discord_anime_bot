package game.shop.items;

import game.AnimeCardsGame;
import game.player_objects.ArmorItemPersonal;
import game.DescriptionDisplayable;
import game.Player;
import game.materials.Material;
import game.materials.MaterialsSet;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Map;

@Entity
public class ArmorItem extends AbstractShopItem implements DescriptionDisplayable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id = -1;
    @Column(unique = true)
    private String name;
    private float armorValue = 0;

    @Embedded
    private MaterialsSet itemCost = new MaterialsSet();

    public ArmorItem() {
        super(null, null, null);
    }

    public ArmorItem(String name, float armorValue, Map<Material, Integer> materials) {
        this(name, armorValue, new MaterialsSet(materials));
    }

    public ArmorItem(String name, float armorValue, MaterialsSet itemCost) {
        super(null, null, null);
        this.name = name;
        this.armorValue = armorValue;
        this.itemCost = itemCost;
    }

    @Override
    public void useFor(AnimeCardsGame game, Player player) {
        Session s = game.getDatabaseSession();
        s.beginTransaction();

        ArmorItemPersonal itemPersonal = new ArmorItemPersonal(this);
        s.save(itemPersonal);
        player = s.load(Player.class, player.getId());
        player.getArmorItems().add(itemPersonal);

        s.getTransaction().commit();
    }

    @Override
    public String getId() {
        return id == -1 ? null : String.valueOf(id);
    }

    @Override
    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getStatsString() {
        return "armor: " + armorValue;
    }

    public float getArmorValue() {
        return armorValue;
    }

    public void setArmorValue(float armorValue) {
        this.armorValue = armorValue;
    }

    @Override
    public MaterialsSet getItemCost() {
        return itemCost;
    }

    @Override
    public void setItemCost(MaterialsSet itemCost) {
        this.itemCost = itemCost;
    }
}
