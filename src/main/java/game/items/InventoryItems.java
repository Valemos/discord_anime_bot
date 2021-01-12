package game.items;

import game.Player;

import java.util.ArrayList;
import java.util.List;

public class InventoryItems {

    private List<ItemGlobal> items = new ArrayList<>();
    private String playerId;

    public InventoryItems(String playerId) {
        this.playerId = playerId;
    }

    public void addItem(ItemGlobal item) {
        items.add(item);
    }
}
