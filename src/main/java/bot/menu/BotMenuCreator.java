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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.function.Function;

public class BotMenuCreator {

    public static Paginator menuShop(EventWaiter eventWaiter, AbstractShop shop, User user){
        List<ItemGlobal> shopItems = shop.getItems();

        Paginator.Builder builder = new Paginator.Builder()
                .setEventWaiter(eventWaiter)
                .setUsers(user)
                .setText(shop.getTitle())
                .setItemsPerPage(5)
                .waitOnSinglePage(true);

        if (shopItems.size() > 0){
            builder.setItems(shopItems.stream()
                    .map(ItemGlobal::getFullDescription)
                    .toArray(String[]::new));
        }else{
            builder.setItems("No items in the shop");
        }

        return builder.build();
    }


    public static void menuForCardsTop(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        menuWithMapper(cards, event, game, "Cards top",
                DisplayableStats::getIdNameStats, selectedPage
        );
    }

    public static void menuForCardIds(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        menuWithMapper(cards, event, game, "Card ids list",
                DisplayableStats::getIdName, selectedPage
        );
    }

    public static void menuForCardStats(List<CardGlobal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        menuWithMapper(cards, event, game, "Cards list",
                DisplayableStats::getNameStats, selectedPage
        );
    }

    public static void menuForPersonalCardStats(List<CardPersonal> cards, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        menuWithMapper(cards, event, game, "Cards collection",
                DisplayableStats::getIdNameStats, selectedPage
        );
    }

    public static void menuForItemStats(List<ItemGlobal> items, CommandEvent event, AnimeCardsGame game, int selectedPage) {
        menuWithMapper(items, event, game, "Items list",
                DisplayableStats::getNameStats, selectedPage
        );
    }

    private static void menuWithMapper(List<? extends DisplayableStats> cards,
                                       CommandEvent event,
                                       AnimeCardsGame game,
                                       String title,
                                       Function<DisplayableStats, String> mapper,
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

    public static void menuDropCards(List<CardGlobal> cards, AnimeCardsGame game, CommandEvent event, EmojiMenuHandler emojiMenu) {

        StringBuilder description = new StringBuilder();
        int counter = 1;
        for (CardGlobal card : cards){
            description.append(counter++).append(". ").append(card.getNameStats()).append('\n');
        }

        EventHandlerButtonMenu menu = new EventHandlerButtonMenu.Builder()
                .setEventWaiter(game.getEventWaiter())
                .setText("Drop cards")
                .setDescription(description.toString())
                .setUsers(event.getAuthor())
                .setChoices(MenuEmoji.ONE, MenuEmoji.TWO, MenuEmoji.THREE)
                .setAction(e -> emojiMenu.hReactionAddEvent(e, game))
                .build();

        Message message = menu.getMessage();
        event.getChannel().sendMessage(message).queue(
                resultMessage -> {
                    menu.display(resultMessage);
                    game.getDropManager().add(resultMessage.getId(), cards);
                }
        );
    }
}
