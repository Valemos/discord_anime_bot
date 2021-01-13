package bot.commands.user.wishlist;

import bot.commands.AbstractCommand;
import bot.commands.MultipleIdentifiersArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import game.wishlist.WishList;

public class WishCardByIdCommand extends AbstractCommand<MultipleIdentifiersArguments> {

    public WishCardByIdCommand(AnimeCardsGame game) {
        super(game, MultipleIdentifiersArguments.class);
        name = "wishid";
        aliases = new String[]{"wi"};
        guildOnly = true;
    }

    @Override
    protected void handle(CommandEvent event) {
        boolean anyCardAdded = false;

        WishList wishList = game.getWishList(player.getId());
        for (String cardId : commandArgs.multipleIds){
            CardGlobal card = game.getCardGlobalById(cardId);
            if (card != null){
                wishList.add(card);
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
