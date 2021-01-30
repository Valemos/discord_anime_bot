package bot.commands.user.wishlist;

import bot.commands.AbstractCommand;
import bot.menu.BotMenuCreator;
import bot.commands.arguments.MultipleWordsArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;

import java.util.List;

public class WishCardCommand extends AbstractCommand<MultipleWordsArguments> {

    public WishCardCommand(AnimeCardsGame game) {
        super(game, MultipleWordsArguments.class);
        name = "wish";
        aliases = new String[]{"w"};
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {
        String cardName = commandArgs.getString();
        List<CardGlobal> cards = game.getCardsGlobal().getFiltered(cardName, null);

        if (cards.isEmpty()){
            sendMessage(event, "no cards found with name \"" + cardName + '\n');
        }
        else if (cards.size() == 1){
            game.addToWishlist(player, cards.get(0));
            sendMessage(event, "added to wishlist" + cards.get(0).getCharacterInfo().getFullName());
        }else{
            BotMenuCreator.menuForCardIds(cards, event, game, 1);
        }
    }

}
