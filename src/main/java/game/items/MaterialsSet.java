package game.items;

import game.Material;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class MaterialsSet {

    EnumMap<Material, Integer> materialAmounts;
    public static final String noMaterialsDescription = "No materials";


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
            addAmount(material, -other.getAmount(material));
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

    public void addAmount(Material material, int increment) {
        int newAmount = getAmount(material) + increment;
        setAmount(material, newAmount);
    }

    public void addMaterials(MaterialsSet materials) {
        for (Material m : Material.values()){
            addAmount(m, materials.getAmount(m));
        }
    }

    public String getDescriptionMultiline() {
        String description = Arrays.stream(Material.values())
                .map(this::getMaterialAmountString)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));
        return description.equals("") ? noMaterialsDescription : description;
    }

    private String getMaterialAmountString(Material material) {
        int amount = getAmount(material);
        if(amount > 0){
            return material.getName() + ": " + amount;
        }
        return null;
    }
}
