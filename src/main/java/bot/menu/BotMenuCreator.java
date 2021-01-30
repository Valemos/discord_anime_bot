package bot.menu;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;
import game.player_objects.ArmorItemPersonal;
import game.DescriptionDisplayable;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.shop.AbstractShop;
import game.shop.items.AbstractShopItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BotMenuCreator {

    public static Paginator menuShop(EventWaiter eventWaiter, AbstractShop shop, User user){
        List<? extends AbstractShopItem> shopItems = shop.getItems();

        Paginator.Builder builder = new Paginator.Builder()
                .setEventWaiter(eventWaiter)
                .setUsers(user)
                .setText(shop.getTitle())
                .setItemsPerPage(5)
                .setTimeout(-1, TimeUnit.MILLISECONDS)
                .waitOnSinglePage(true);

        if (shopItems.size() > 0){
            builder.setItems(shopItems.stream()
                    .map(AbstractShopItem::getFullDescription)
                    .toArray(String[]::new));
        }else{
            builder.setItems("No items in the shop");
        }

        return builder.build();
    }

    public static void menuForCardsTop(Collection<CardGlobal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        showPagedMenu(event, game, "Cards top", selectedPage,
                cards.stream()
                .map(DescriptionDisplayable::getIdNameStats)
                .toArray(String[]::new)
        );
    }

    public static void menuForCardIds(Collection<CardGlobal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        showPagedMenu(event, game, "Card ids list", selectedPage,
                cards.stream()
                .map(DescriptionDisplayable::getIdName)
                .toArray(String[]::new)
        );
    }

    public static void menuForCardStats(Collection<CardGlobal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        showPagedMenu(event, game, "Cards list", selectedPage,
                cards.stream()
                .map(DescriptionDisplayable::getNameStats)
                .toArray(String[]::new)
        );
    }

    public static void menuForPersonalCardStats(Collection<CardPersonal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        showPagedMenu(event, game, "Cards collection", selectedPage,
                cards.stream()
                .map(DescriptionDisplayable::getIdNameStats)
                .toArray(String[]::new)
        );
    }

    public static void menuForItemStats(Collection<ArmorItemPersonal> items, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        showPagedMenu(event, game, "Items list", selectedPage,
                items.stream()
                .map(DescriptionDisplayable::getNameStats)
                .toArray(String[]::new)
        );
    }

    public static void showPagedMenu(CommandEvent event,
                                     AnimeCardsGame game,
                                     String title, int selectedPage, String[] itemsArray){

        if (itemsArray.length > 0){
            Paginator cardsMenu = new Paginator.Builder()
                    .setEventWaiter(game.getEventWaiter())
                    .setText('`' + title + '`')
                    .setUsers(event.getAuthor())
                    .waitOnSinglePage(true)
                    .setItems(itemsArray)
                    .setItemsPerPage(10)
                    .setTimeout(5, TimeUnit.MINUTES)
                    .build();

            cardsMenu.paginate(event.getChannel(), selectedPage);
        }else{
            Message message = new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                                    .setTitle(title)
                                    .setDescription("Empty")
                                    .build()
                    ).build();
            event.getChannel().sendMessage(message).queue();
        }
    }
}
