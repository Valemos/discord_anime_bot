package bot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import game.items.ItemGlobal;
import game.shop.AbstractShop;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.function.Function;

public class MenuCreator {

    public static Paginator getShopMenu(EventWaiter eventWaiter, AbstractShop shop, User user){
        List<ItemGlobal> shopItems = shop.getItems();

        Paginator.Builder builder = new Paginator.Builder();
        builder.setEventWaiter(eventWaiter);
        builder.setUsers(user);
        builder.setText(shop.getTitle());

        if (shopItems.size() > 0){
            builder.setItems(shopItems.stream()
                    .map(ItemGlobal::getOneLineString)
                    .toArray(String[]::new));
        }else{
            builder.setItems("No items in the shop");
        }

        builder.setItemsPerPage(5);
        return builder.build();
    }

    public static void showMenuForCardIds(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game) {
        showMenuForCardsWithMapper(cards, event, game, "Card ids list",
                card -> card.getId() + " " + card.getFullName()
        );
    }

    public static void showMenuForCardStats(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game) {
        showMenuForCardsWithMapper(cards, event, game, "Cards list",
                CardGlobal::getOneLineStatsString
        );
    }

    private static void showMenuForCardsWithMapper(List<CardGlobal> cards,
                                                   CommandEvent event,
                                                   AnimeCardsGame game,
                                                   String title,
                                                   Function<CardGlobal, String> mapper){

        Paginator cardsMenu = new Paginator.Builder()
                .setEventWaiter(game.getEventWaiter())
                .setText(title)
                .setUsers(event.getAuthor())
                .setItems(cards.stream()
                        .map(mapper)
                        .toArray(String[]::new))
                .setItemsPerPage(5)
                .build();

        cardsMenu.display(event.getChannel());
    }
}
