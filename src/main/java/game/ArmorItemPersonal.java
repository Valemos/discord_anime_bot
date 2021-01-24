package game;

import bot.Base36SequenceGenerator;
import game.shop.items.ArmorItem;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ArmorItemPersonal implements DescriptionDisplayable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base36_armor_personal")
    @GenericGenerator(name = "base36_armor_personal", strategy = "bot.Base36SequenceGenerator")
    private String id;

    @OneToOne
    private ArmorItem original;

    public ArmorItemPersonal() {
    }

    public ArmorItemPersonal(ArmorItem original) {
        this.original = original;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
