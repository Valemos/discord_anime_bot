package bot.commands.user.squadron;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleIdentifiersArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.player_objects.squadron.Squadron;

public class SquadronRemoveCommand extends AbstractCommand<MultipleIdentifiersArguments> {
    public SquadronRemoveCommand(AnimeCardsGame game) {
        super(game, MultipleIdentifiersArguments.class);
        name = "squadronremove";
        aliases = new String[]{"sqr"};
        guildOnly = false;
        help = "removes your personal cards to squadron. must be provided with list of ids";
    }

    @Override
    public void handle(CommandEvent event) {
        Squadron squadron = game.getOrCreateSquadron(player);

        if(squadron.getPatrol().isStarted()){
            sendMessage(event, "stop patrol to edit squadron");
            return;
        }


        if (game.removeSquadronMembers(squadron, commandArgs.multipleIds)){
            sendMessage(event, "removed members");
        }else{
            sendMessage(event, "cannot remove from squadron");
        }
    }
}
