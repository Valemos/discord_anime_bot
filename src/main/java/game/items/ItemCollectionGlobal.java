package game.items;

import java.util.ArrayList;
import java.util.List;

public class ItemCollectionGlobal {
    List<ItemGlobal> items = new ArrayList<>();
    private int currentItemId = 0;

    public ItemCollectionGlobal() {
    }

    public List<ItemGlobal> getItems() {
        return items;
    }

    public void addItem(ItemGlobal item) {
        if (item.getStringId() == null){
            item.setId(getNextItemId());
            items.add(item);
        }
    }

    private String getNextItemId() {
        return String.valueOf(++currentItemId);
    }

    public boolean removeById(String itemId) {
        return items.removeIf((item) -> item.getStringId().equals(itemId));
    }

    public ItemGlobal getById(String itemId) {
        return items.stream()
                .filter((item) -> item.getId().equals(itemId)).findFirst().orElse(null);
    }
}
