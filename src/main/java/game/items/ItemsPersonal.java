package game.items;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsPersonal extends ArrayList<ItemGlobal> {
    public ItemGlobal getById(String itemId) {
        return stream().filter(i -> i.getId().equals(itemId)).findFirst().orElse(null);
    }

    public List<ItemGlobal> sortedByPower() {
        return stream().sorted(
                (i1, i2) -> Float.compare(
                        i1.getItemPower(),
                        i2.getItemPower()
                )
        ).collect(Collectors.toList());
    }
}
