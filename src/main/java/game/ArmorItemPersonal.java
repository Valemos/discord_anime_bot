package game;

import bot.ShortUUID;
import game.shop.items.ArmorItem;

import javax.persistence.*;

@Entity
public class ArmorItemPersonal implements DescriptionDisplayable {

    @Id
    private String id;

    @OneToOne
    private ArmorItem original;

    public ArmorItemPersonal() {
    }

    public ArmorItemPersonal(ArmorItem original) {
        this.original = original;
    }

    @PrePersist
    void prePersist() {
        id = ShortUUID.generate();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
