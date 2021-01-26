package game.player_objects;

import game.DescriptionDisplayable;
import game.Player;
import game.shop.items.ArmorItem;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ArmorItemPersonal implements DescriptionDisplayable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base36_armor_personal")
    @GenericGenerator(name = "base36_armor_personal", strategy = "bot.Base36SequenceGenerator")
    private String id;

    @ManyToOne
    private Player owner;

    @OneToOne
    private ArmorItem original;

    public ArmorItemPersonal() {
    }

    public ArmorItemPersonal(Player owner, ArmorItem original) {
        this.owner = owner;
        this.original = original;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public String getName() {
        return original.getName();
    }

    public ArmorItem getOriginal() {
        return original;
    }

    public void setOriginal(ArmorItem original) {
        this.original = original;
    }

    @Override
    public String getStatsString() {
        return original.getStatsString();
    }
}
