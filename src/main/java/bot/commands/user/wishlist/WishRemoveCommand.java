package bot.commands.user.wishlist;

import bot.commands.AbstractCommand;
import bot.menu.BotMenuCreator;
import bot.commands.arguments.MultipleWordsArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;

import java.util.List;

public class WishRemoveCommand extends AbstractCommand<MultipleWordsArguments> {

    public WishRemoveCommand(AnimeCardsGame game) {
        super(game, MultipleWordsArguments.class);
        name = "wishremove";
        aliases = new String[]{"wr"};
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {
        String cardName = commandArgs.getSingleString();

        List<CardGlobal> cards = game.getCardsGlobal().getFiltered(cardName, null);
        if (cards.size() == 0){
            sendMessage(event, "no cards found with name \"" + cardName + '\n');
        }
        else if (cards.size() == 1){
            CardGlobal card = cards.get(0);
            if(game.removeFromWishlist(player, card)){
                sendMessage(event, "removed from wishlist " + card.getCharacterInfo().getFullName());
            }else{
                sendMessage(event, card.getCharacterInfo().getFullName() + " not found in wishlist");
            }
        }else{
            BotMenuCreator.menuForCardIds(cards, event, game, 1);
        }
    }
}
