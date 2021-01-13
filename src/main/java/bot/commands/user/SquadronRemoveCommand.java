package bot.commands.user;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.squadron.Squadron;

public class SquadronRemoveCommand extends AbstractCommand<MultipleCardsArguments> {
    public SquadronRemoveCommand(AnimeCardsGame game) {
        super(game, MultipleCardsArguments.class);
        name = "squadronremove";
        aliases = new String[]{"sqr"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        Squadron squadron = game.getSquadron(player);

        if (squadron.getCards().removeIf(
                (card) -> commandArgs.cardIds.contains(card.getCardId())
        )){
            sendMessage(event, "removed cards");
        }else{
            sendMessage(event, "cannot remove any card");
        }
    }
}
