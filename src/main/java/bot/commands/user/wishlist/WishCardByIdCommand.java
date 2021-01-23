package bot.commands.user.wishlist;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleIdentifiersArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;

public class WishCardByIdCommand extends AbstractCommand<MultipleIdentifiersArguments> {

    public WishCardByIdCommand(AnimeCardsGame game) {
        super(game, MultipleIdentifiersArguments.class);
        name = "wishid";
        aliases = new String[]{"wi"};
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {
        boolean anyCardAdded = false;

        for (String cardId : commandArgs.multipleIds){
            CardGlobal card = game.getCardsGlobal().getById(cardId);
            if (card != null){
                game.addToWishlist(player, card);
                anyCardAdded = true;
            }
        }

        if(anyCardAdded){
            sendMessage(event, "cards added");
        }else{
            sendMessage(event, "cannot add any card");
        }
    }
}
