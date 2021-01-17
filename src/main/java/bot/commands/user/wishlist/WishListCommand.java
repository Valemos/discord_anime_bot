package bot.commands.user.wishlist;

import bot.commands.AbstractCommandOptionalPlayer;
import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.wishlist.WishList;

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

        WishList wishList = game.getWishList(requestedPlayer.getId());
        BotMenuCreator.menuForCardStats(wishList.getCards(), event, game, 1);
    }
}
