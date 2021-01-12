package game.items;

import game.Material;

import java.util.EnumMap;

public class MaterialsSet {

    EnumMap<Material, Integer> materialAmounts;

    public MaterialsSet() {
        this(new EnumMap<>(Material.class));
    }

    public MaterialsSet(EnumMap<Material, Integer> materialAmounts) {
        this.materialAmounts = materialAmounts;
    }

    public void setAmount(Material material, int newAmount){
        materialAmounts.put(material, newAmount);
    }

    public int getAmount(Material material){
        return materialAmounts.getOrDefault(material, 0);
    }

    public void subtractMaterials(MaterialsSet otherMaterials) {
        // TODO Subtract hashmaps
    }

    public boolean containsNotLessThan(MaterialsSet itemCost) {
        // TODO compare hashmaps
        return true;
    }

    public void incrementAmount(Material material, int increment) {
        int newAmount = materialAmounts.getOrDefault(material, 0) + increment;
        materialAmounts.put(material, newAmount);
    }
}
