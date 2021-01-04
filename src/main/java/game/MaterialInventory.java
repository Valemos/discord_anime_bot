package game;

import java.util.EnumMap;


public class MaterialInventory {
    EnumMap<Material, Integer> materialCounts;

    public MaterialInventory() {
        materialCounts = new EnumMap<>(Material.class);
        clear();
    }

    private void clear() {
        for (Material material: Material.values()) {
            materialCounts.put(material, 0);
        }
    }
}
