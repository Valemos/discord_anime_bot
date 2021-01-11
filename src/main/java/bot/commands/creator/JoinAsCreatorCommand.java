package bot.commands.creator;

import bot.CommandAccessLevel;
import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class JoinAsCreatorCommand extends AbstractCommand<AbstractCommand.NoArgumentsConfig> {

    public JoinAsCreatorCommand(AnimeCardsGame game) {
        super(game, NoArgumentsConfig.class);
        name = "joincreator";
        aliases = new String[]{"jc"};
    }

    @Override
    protected void handle(CommandEvent event) {
        if (player.getAccessLevel() != CommandAccessLevel.CREATOR){
            player.setAccessLevel(CommandAccessLevel.CREATOR);
            event.getChannel().sendMessage("you are now the creator").queue();
        }else{
            event.getChannel().sendMessage("you are already the creator").queue();
        }
    }
}
