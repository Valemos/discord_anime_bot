package game.items;

import game.Player;

import java.util.ArrayList;
import java.util.List;

public class ItemCollectionsPersonal {

    private List<ItemGlobal> items = new ArrayList<>();

    public ItemCollectionsPersonal() {
    }

    public void addItem(ItemGlobal item) {
        items.add(item);
    }


}
