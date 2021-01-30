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
        StringBuilder message = new StringBuilder();

        for (String cardId : commandArgs.multipleIds){
            CardGlobal card = game.getCardsGlobal().getById(cardId);
            if (card != null){
                if (!player.getWishList().contains(card)){
                    game.addToWishlist(player, card);
                    anyCardAdded = true;
                }else{
                    message.append("you already added \"").append(card.getName()).append("\"\n");
                }
            }else{
                message.append("cannot find card ").append(cardId).append('\n');
            }
        }

        if(anyCardAdded) message.append("cards added");

        if(!message.isEmpty()) sendMessage(event, message.toString());
    }
}
