package game.items;

public class ItemGlobal implements game.DisplayableStats {
    private String id;
    private final String name;
    private final float attack;
    private final float defense;
    private final String description;
    private final MaterialsSet cost;


    public ItemGlobal(String name, int attack, int defense) {
        this(name, attack, defense, "", new MaterialsSet());
    }

    public ItemGlobal(String name, float attack, float defense, String description, MaterialsSet cost) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.description = description;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOneLineString() {
        return String.format("%s: %s attack %s defense", name, attack, defense);
    }

    public float getAttack() {
        return attack;
    }

    public float getDefense() {
        return defense;
    }

    public MaterialsSet getMaterialsCost() {
        return cost;
    }

    public float getItemPower() {
        return attack + defense;
    }

    @Override
    public String getNameStatsString() {
        if (attack <= 0){
            return name + ": d-" + defense;
        }else if (defense <= 0){
            return name + ": a-" + attack;
        }else{
            return name + ": a-" + attack + " d-" + defense;
        }
    }

    @Override
    public String getIdName() {
        return id + " - " + name;
    }
}
