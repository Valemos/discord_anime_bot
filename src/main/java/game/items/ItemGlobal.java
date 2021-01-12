package game.items;

public class ItemGlobal {
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

    public float getDefense() {
        return defense;
    }

    public MaterialsSet getMaterialsCost() {
        return cost;
    }
}
