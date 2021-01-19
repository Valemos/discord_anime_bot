package bot.commands.user.squadron;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleIdentifiersArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.squadron.Squadron;

public class SquadronRemoveCommand extends AbstractCommand<MultipleIdentifiersArguments> {
    public SquadronRemoveCommand(AnimeCardsGame game) {
        super(game, MultipleIdentifiersArguments.class);
        name = "squadronremove";
        aliases = new String[]{"sqr"};
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        Squadron squadron = game.getSquadron(player);

        if (squadron.getCards().removeIf(
                (card) -> commandArgs.multipleIds.contains(card.getId())
        )){
            sendMessage(event, "removed cards");
        }else{
            sendMessage(event, "cannot remove any card");
        }
    }
}
