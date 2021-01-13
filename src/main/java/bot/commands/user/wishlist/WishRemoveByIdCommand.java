package bot.commands.user.wishlist;

import bot.commands.AbstractCommand;
import bot.commands.MultipleIdentifiersArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.wishlist.WishList;

public class WishRemoveByIdCommand extends AbstractCommand<MultipleIdentifiersArguments> {

    public WishRemoveByIdCommand(AnimeCardsGame game) {
        super(game, MultipleIdentifiersArguments.class);
        name = "wishremoveid";
        aliases = new String[]{"wri"};
        guildOnly = true;
    }

    @Override
    protected void handle(CommandEvent event) {
        boolean anyCardRemoved = false;

        WishList wishList = game.getWishList(player.getId());
        for (String cardId : commandArgs.multipleIds){
            if (wishList.removeById(cardId)){
                anyCardRemoved = true;
            }
        }

        if(anyCardRemoved){
            sendMessage(event, "cards removed");
        }else{
            sendMessage(event, "cannot remove any card");
        }
    }
}
