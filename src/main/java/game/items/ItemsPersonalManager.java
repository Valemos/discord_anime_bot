package game.items;

import game.MappedObjectManager;
import org.jetbrains.annotations.NotNull;

public class ItemsPersonalManager extends MappedObjectManager<String, ItemsPersonal> {

    public ItemsPersonalManager() {
        super(ItemsPersonal.class);
    }

    public void addItem(String playerId, ItemGlobal item) {
        @NotNull ItemsPersonal items = getElementOrCreate(playerId);
        items.add(item);
    }

    public ItemGlobal getItem(String playerId, String itemId){
        @NotNull ItemsPersonal items = getElementOrCreate(playerId);
        return items.getById(itemId);
    }
}
