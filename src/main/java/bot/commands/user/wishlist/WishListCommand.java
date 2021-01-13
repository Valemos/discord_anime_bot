package bot.commands.user.wishlist;

import bot.commands.AbstractCommand;
import bot.commands.MenuCreator;
import bot.commands.OptionalIdentifierArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.wishlist.WishList;

public class WishListCommand extends AbstractCommand<OptionalIdentifierArguments> {

    public WishListCommand(AnimeCardsGame game) {
        super(game, OptionalIdentifierArguments.class);
        name = "wishlist";
        aliases = new String[]{"wl"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        String playerId = commandArgs.getSelectedOrPlayerId(player);

        WishList wishList = game.getWishList(playerId);
        MenuCreator.showMenuForCardStats(wishList.getCards(), event, game);
    }
}
