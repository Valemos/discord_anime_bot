package bot.commands.handlers.creator;

import bot.PlayerAccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.AbstractBotCommand;
import bot.commands.handlers.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class JoinAsCreatorCommand extends AbstractCommand {

    public JoinAsCreatorCommand(AnimeCardsGame game) {
        super(game);
        name = "joincreator";
        aliases = new String[]{"jc"};
    }

    @Override
    protected void execute(CommandEvent event) {
        if (parameters.player.getAccessLevel() != PlayerAccessLevel.CREATOR){
            parameters.player.setAccessLevel(PlayerAccessLevel.CREATOR);
            event.getChannel().sendMessage("you are now the creator").queue();
        }else{
            event.getChannel().sendMessage("you are already the creator").queue();
        }
    }
}
