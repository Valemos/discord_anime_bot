package game;

import bot.ShortUUID;
import game.shop.items.ArmorItem;

import javax.persistence.*;

@Entity
public class ArmorItemPersonal implements DisplayableStats {
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

    @Id
    public String getId() {
        return id;
    }

    public ArmorItem getOriginal() {
        return original;
    }

    public void setOriginal(ArmorItem original) {
        this.original = original;
    }

    @Override
    public String getNameStats() {
        return original.getName() + " armor:" + original.getArmorValue();
    }

    @Override
    public String getIdName() {
        // TODO: finish
        return null;
    }

    @Override
    public String getIdNameStats() {
        // TODO: finish
        return null;
    }

    @Override
    public String getFullDescription() {
        // TODO: finish
        return null;
    }
}
