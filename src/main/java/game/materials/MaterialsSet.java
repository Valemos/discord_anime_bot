package game.materials;

import javax.persistence.*;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class MaterialsSet {

    @ElementCollection
    @MapKeyColumn(name = "materialtype")
    Map<Material, Integer> materials = new EnumMap<>(Material.class);

    @Transient
    public static final String noMaterialsDescription = "No materials";


    public MaterialsSet() {
    }

    public MaterialsSet(Map<Material, Integer> materials) {
        this.materials = materials;
    }

    public void setMaterials(Map<Material, Integer> materials) {
        this.materials = materials;
    }

    public Map<Material, Integer> getMaterials() {
        return materials;
    }

    public boolean isEmpty() {
        return materials.isEmpty();
    }

    public void setAmount(Material material, int newAmount){
        materials.put(material, newAmount);
    }

    public int getAmount(Material material){
        return materials.getOrDefault(material, 0);
    }

    public void subtractMaterials(MaterialsSet other) {
        for(Material material : Material.values()){
            addAmount(material, -other.getAmount(material));
        }
    }

    public boolean containsEnough(MaterialsSet other) {
        for(Material material : Material.values()){
            if (this.getAmount(material) < other.getAmount(material)){
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

    @Transient
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
