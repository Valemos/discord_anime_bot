package bot.commands.handlers.creator;

import bot.AccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.BotCommandHandler;

public class JoinAsCreatorHandler extends BotCommandHandler {

    public JoinAsCreatorHandler() {
        super(new CommandInfo("makecreator"));
    }

    @Override
    public void handle(CommandParameters parameters) {
        if (parameters.player.getAccessLevel() != AccessLevel.CREATOR){
            parameters.player.setAccessLevel(AccessLevel.CREATOR);
            accessLevel = AccessLevel.CREATOR;
            parameters.channel.sendMessage("you are now the creator").queue();
        }else{
            parameters.channel.sendMessage("you are already the creator").queue();
        }
    }
}
