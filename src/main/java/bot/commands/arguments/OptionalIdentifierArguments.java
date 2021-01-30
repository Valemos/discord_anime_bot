package bot.commands.arguments;

import com.jagrosh.jdautilities.command.CommandEvent;
import game.Player;
import org.kohsuke.args4j.Argument;

public class OptionalIdentifierArguments {

    @Argument(metaVar = "identifier", usage = "optional id to use command for specific player")
    public String id;
}
