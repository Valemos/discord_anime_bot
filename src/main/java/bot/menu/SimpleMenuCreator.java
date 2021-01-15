package bot.menu;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;
import game.DisplayableStats;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.items.ItemGlobal;
import game.shop.AbstractShop;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.function.Function;

public class SimpleMenuCreator {

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


    public static void showMenuForCardsTop(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game) {
        showMenuWithMapper(cards, event, game, "Cards top",
                DisplayableStats::getIdNameStats
        );
    }

    public static void showMenuForCardIds(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game) {
        showMenuWithMapper(cards, event, game, "Card ids list",
                DisplayableStats::getIdName
        );
    }

    public static void showMenuForCardStats(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game) {
        showMenuWithMapper(cards, event, game, "Cards list",
                DisplayableStats::getNameStats
        );
    }

    public static void showMenuForPersonalCardStats(List<CardPersonal> cards, CommandEvent event, AnimeCardsGame game) {
        showMenuWithMapper(cards, event, game, "Cards collection",
                DisplayableStats::getIdNameStats
        );
    }

    public static void showMenuForItemStats(List<ItemGlobal> items, CommandEvent event, AnimeCardsGame game) {
        showMenuWithMapper(items, event, game, "Items list",
                DisplayableStats::getNameStats
        );
    }

    private static void showMenuWithMapper(List<? extends DisplayableStats> cards,
                                           CommandEvent event,
                                           AnimeCardsGame game,
                                           String title,
                                           Function<DisplayableStats, String> mapper){

        if (cards.size() > 0){
            Paginator cardsMenu = new Paginator.Builder()
                    .setEventWaiter(game.getEventWaiter())
                    .setText(title)
                    .setUsers(event.getAuthor())
                    .waitOnSinglePage(true)
                    .setItems(cards.stream()
                            .map(mapper)
                            .toArray(String[]::new))
                    .setItemsPerPage(5)
                    .build();

            cardsMenu.display(event.getChannel());
        }else{
            event.getChannel().sendMessage(
                    new MessageBuilder()
                            .append(title)
                            .setEmbed(
                                new EmbedBuilder()
                                        .setDescription("No cards")
                                        .build())
                            .build()
            ).queue();
        }
    }
}
