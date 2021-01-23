package bot.commands.user.squadron;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.player_objects.squadron.PatrolType;
import game.player_objects.squadron.Squadron;
import org.kohsuke.args4j.Argument;

import java.time.Instant;

public class PatrolCommand extends AbstractCommand<PatrolCommand.Arguments> {
    public static class Arguments{
        PatrolType patrolType;

        @Argument(metaVar = "exploration world \"overworld\" (\"o\") or \"underworld\" (\"u\")",
                usage = "type of world to send current squadron to",
                required = true)
        public void setPatrolWorld(PatrolType patrolType) {
            // replace aliases for full names
            if (PatrolType.O == patrolType){
                this.patrolType = PatrolType.OVERWORLD;
            }else if (PatrolType.U == patrolType){
                this.patrolType = PatrolType.UNDERWORLD;
            }else{
                this.patrolType = patrolType;
            }
        }
    }

    public PatrolCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "patrol";
        aliases = new String[]{"p"};
    }

    @Override
    public void handle(CommandEvent event) {
        Squadron squadron = game.getOrCreateSquadron(player);
        if (squadron.isEmpty()){
            sendMessage(event, "your squadron is empty");

        } else if (squadron.getPatrol().isStarted()) {
            sendMessage(event,
                    "you have already active patrol in "
                            + squadron.getPatrol().getPatrolType().getTypeName() + ":\n"
                            + squadron.getDescription());

        } else {
            game.createNewPatrol(player, commandArgs.patrolType, Instant.now());

            sendMessage(event,
                    "your heroes started " +
                            commandArgs.patrolType.getTypeName() +
                            " exploration");
        }
    }
}
