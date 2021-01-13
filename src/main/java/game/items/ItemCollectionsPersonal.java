package game.items;

import game.Player;

import java.util.ArrayList;
import java.util.List;

public class ItemCollectionsPersonal {

    private final List<ItemGlobal> items;

    public ItemCollectionsPersonal() {
        this(new ArrayList<>());
    }

    public ItemCollectionsPersonal(List<ItemGlobal> items) {
        this.items = items;
    }

    public void addItem(ItemGlobal item) {
        items.add(item);
    }

}
