package game.shop;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;
import game.items.ItemGlobal;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class ShopViewer {

    public static Paginator buildPagesMenu(EventWaiter eventWaiter, AnimeCardsGame game, User user){
        List<ItemGlobal> shopItems = game.getItemsCollection().getItems();

        Paginator.Builder builder = new Paginator.Builder();
        builder.setEventWaiter(eventWaiter);
        builder.setUsers(user);

        if (shopItems.size() > 0){
            builder.setItems(shopItems.stream()
                    .map(ItemGlobal::getOneLineString)
                    .toArray(String[]::new));
        }else{
            builder.setItems("No items in the shop");
        }

        builder.setItemsPerPage(4);
        return builder.build();
    }
}
