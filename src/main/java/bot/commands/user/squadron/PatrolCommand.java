package bot.commands.user.squadron;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.squadron.PatrolType;
import org.kohsuke.args4j.Argument;

import java.time.Instant;

public class PatrolCommand extends AbstractCommand<PatrolCommand.Arguments> {
    public static class Arguments{
        @Argument(metaVar = "exploration world \"overworld\" or \"underworld\"", required = true, usage = "type of world to send current squadron to")
        PatrolType patrolType;
    }

    public PatrolCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "patrol";
        aliases = new String[]{"p"};
    }

    @Override
    public void handle(CommandEvent event) {
        if (game.createNewPatrol(player, commandArgs.patrolType, Instant.now())){
            sendMessage(event, "your heroes started " + commandArgs.patrolType.getTypeName() + " exploration");
        }else{
            sendMessage(event, "cannot start patrol");
        }
    }
}
