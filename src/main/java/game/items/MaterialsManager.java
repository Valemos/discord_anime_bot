package game.items;

import java.util.HashMap;

public class MaterialsManager {

    HashMap<String, MaterialsSet> materialsMap = new HashMap<>();

    public MaterialsSet createEmptyMaterials(String playerId) {
        MaterialsSet materials = new MaterialsSet();
        materialsMap.put(playerId, materials);
        return materials;
    }

    public MaterialsSet get(String playerId) {
        MaterialsSet materials = materialsMap.getOrDefault(playerId, null);
        if (materials == null){
            materials = createEmptyMaterials(playerId);
        }
        return materials;
    }
}
