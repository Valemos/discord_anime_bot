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

    public void subtractMaterials(MaterialsSet other) {
        for(Material material : Material.values()){
            incrementAmount(material, -other.getAmount(material));
        }
    }

    public boolean containsNotLessThan(MaterialsSet other) {
        for(Material material : Material.values()){
            if (getAmount(material) < other.getAmount(material)){
                return false;
            }
        }
        return true;
    }

    public void incrementAmount(Material material, int increment) {
        int newAmount = getAmount(material) + increment;
        setAmount(material, newAmount);
    }
}
