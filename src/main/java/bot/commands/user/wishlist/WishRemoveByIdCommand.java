package bot.commands.user.wishlist;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleIdentifiersArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class WishRemoveByIdCommand extends AbstractCommand<MultipleIdentifiersArguments> {

    public WishRemoveByIdCommand(AnimeCardsGame game) {
        super(game, MultipleIdentifiersArguments.class);
        name = "wishremoveid";
        aliases = new String[]{"wri"};
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {
        boolean anyCardRemoved = false;

        for (String cardId : commandArgs.multipleIds){
            if (game.removeFromWishlist(player, cardId)){
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
