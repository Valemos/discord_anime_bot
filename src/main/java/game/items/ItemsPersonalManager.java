package game.items;

import game.MappedObjectManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemsPersonalManager extends MappedObjectManager<String, ItemsPersonal> {

    public ItemsPersonalManager() {
        super(ItemsPersonal.class);
    }

    public void addItem(String playerId, ItemGlobal item) {
        @NotNull ItemsPersonal items = getElement(playerId);
        items.add(item);
    }

    public ItemGlobal getItem(String playerId, String itemId){
        @NotNull ItemsPersonal items = getElement(playerId);
        return items.getById(itemId);
    }
}
