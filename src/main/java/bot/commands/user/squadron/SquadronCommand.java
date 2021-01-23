package bot.commands.user.squadron;

import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.player_objects.squadron.Squadron;

public class SquadronCommand extends AbstractCommandNoArguments {

    public SquadronCommand(AnimeCardsGame game) {
        super(game);
        name = "squadron";
        aliases = new String[]{"sq"};
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        Squadron squadron = game.getOrCreateSquadron(player);
        if (!squadron.isEmpty()){
            sendMessage(event, squadron.getDescription());
        }else{
            sendMessage(event, "squadron is empty");
        }
    }
}
