package game.materials;

import org.hibernate.SessionFactory;

import java.util.HashMap;

public class MaterialsManager {

    HashMap<String, MaterialsSet> materialsMap = new HashMap<>();

    public MaterialsManager(SessionFactory dbSession) {

    }

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

    public void set(String playerId, MaterialsSet newMaterials) {
        materialsMap.put(playerId, newMaterials);
    }

    public void addMaterials(String playerId, MaterialsSet materials) {
        get(playerId).addMaterials(materials);
    }

    public void subtractMaterials(String playerId, MaterialsSet materials) {
        get(playerId).subtractMaterials(materials);
    }
}
