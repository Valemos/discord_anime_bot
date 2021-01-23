package bot.commands.user.wishlist;

import bot.commands.AbstractCommandOptionalPlayer;
import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;

import java.util.Set;

public class WishListCommand extends AbstractCommandOptionalPlayer {

    public WishListCommand(AnimeCardsGame game) {
        super(game);
        name = "wishlist";
        aliases = new String[]{"wl"};
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        if(tryFindPlayerArgument(event)){
            return;
        }

        Set<CardGlobal> wishList = requestedPlayer.getWishList();
        BotMenuCreator.menuForCardStats(wishList, event, game, 1);
    }
}
