package bot.menu;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;
import game.ArmorItemPersonal;
import game.DescriptionDisplayable;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.shop.AbstractShop;
import game.shop.items.AbstractShopItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.function.Function;

public class BotMenuCreator {

    public static Paginator menuShop(EventWaiter eventWaiter, AbstractShop shop, User user){
        List<? extends AbstractShopItem> shopItems = shop.getItems();

        Paginator.Builder builder = new Paginator.Builder()
                .setEventWaiter(eventWaiter)
                .setUsers(user)
                .setText(shop.getTitle())
                .setItemsPerPage(5)
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


    public static void menuForCardsTop(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        menuWithMapper(cards, event, game, "Cards top",
                DescriptionDisplayable::getIdNameStats, selectedPage
        );
    }

    public static void menuForCardIds(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        menuWithMapper(cards, event, game, "Card ids list",
                DescriptionDisplayable::getIdName, selectedPage
        );
    }

    public static void menuForCardStats(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        menuWithMapper(cards, event, game, "Cards list",
                DescriptionDisplayable::getNameStats, selectedPage
        );
    }

    public static void menuForPersonalCardStats(List<CardPersonal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        menuWithMapper(cards, event, game, "Cards collection",
                DescriptionDisplayable::getIdNameStats, selectedPage
        );
    }

    public static void menuForItemStats(List<ArmorItemPersonal> items, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        menuWithMapper(items, event, game, "Items list",
                DescriptionDisplayable::getNameStats, selectedPage
        );
    }

    private static void menuWithMapper(List<? extends DescriptionDisplayable> cards,
                                       CommandEvent event,
                                       AnimeCardsGame game,
                                       String title,
                                       Function<DescriptionDisplayable, String> mapper,
                                       int selectedPage){

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

            cardsMenu.paginate(event.getChannel(), selectedPage);
        }else{
            Message message = new MessageBuilder()
                    .append(title)
                    .setEmbed(
                            new EmbedBuilder()
                                    .setDescription("Empty")
                                    .build()
                    ).build();
            event.getChannel().sendMessage(message).queue();
        }
    }
}
