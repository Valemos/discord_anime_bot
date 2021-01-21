package bot.commands.user.squadron;

import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.squadron.PatrolActivity;
import game.items.MaterialsSet;
import game.squadron.Squadron;

import java.time.Instant;

public class PatrolStopCommand extends AbstractCommandNoArguments {
    public PatrolStopCommand(AnimeCardsGame game) {
        super(game);
        name = "patrolstop";
        aliases = new String[]{"ps"};
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {
        Squadron squadron = player.getSquadron();
        if (squadron != null){
            MaterialsSet materialsFound = game.finishPatrol(squadron, Instant.now());
            sendMessage(event,
                    "current patrol has finished, you received\n"
                            + materialsFound.getDescriptionMultiline());
        }else{
            sendMessage(event, "you have no busy squadron");
        }
    }
}
