package game.items;

public class ItemGlobal {
    private int id;
    private final String name;
    private final float attack;
    private final float defense;
    private final String description;

    public ItemGlobal(String name, float attack, float defense, String description) {

        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
