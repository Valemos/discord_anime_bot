package bot.commands.arguments;

import game.Player;
import org.kohsuke.args4j.Argument;

public class OptionalIdentifierArguments {

    @Argument(usage = "optional id to use command for specific player")
    private String id;


    public String getSelectedOrPlayerId(Player player) {
        return id == null? player.getId() : id;
    }
}
