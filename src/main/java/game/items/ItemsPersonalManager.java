package game.items;

import java.util.ArrayList;
import java.util.List;

public class ItemsPersonalManager {

    private final List<ItemGlobal> items;

    public ItemsPersonalManager() {
        this(new ArrayList<>());
    }

    public ItemsPersonalManager(List<ItemGlobal> items) {
        this.items = items;
    }

    public void addItem(ItemGlobal item) {
        items.add(item);
    }

}
