package bot.commands.user.squadron;

import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.squadron.PatrolActivity;
import game.items.MaterialsSet;

public class PatrolStopCommand extends AbstractCommandNoArguments {
    public PatrolStopCommand(AnimeCardsGame game) {
        super(game);
        name = "patrolstop";
        aliases = new String[]{"ps"};
        guildOnly = true;
    }

    @Override
    protected void handle(CommandEvent event) {
        PatrolActivity patrol = game.findPatrol(player);
        if (patrol != null){
            MaterialsSet materialsFound = game.finishPatrol(patrol);
            sendMessage(event,
                    "current patrol was finished, you received\n"
                            + materialsFound.getDescriptionMultiline());
        }else{
            sendMessage(event, "you have no active patrol");
        }
    }
}
