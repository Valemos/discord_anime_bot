package game;

import java.util.EnumMap;


public class InventoryMaterial {
    EnumMap<Material, Integer> materialCounts;

    public InventoryMaterial() {
        materialCounts = new EnumMap<>(Material.class);
        clear();
    }

    private void clear() {
        for (Material material: Material.values()) {
            materialCounts.put(material, 0);
        }
    }
}
