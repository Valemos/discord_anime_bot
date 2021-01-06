package bot.commands;

import bot.AccessLevel;

public class JoinAsCreatorHandler extends BotCommandHandler{

    public JoinAsCreatorHandler() {
        commandInfo = new CommandInfo("makecreator");
        accessLevel = AccessLevel.USER;
    }

    @Override
    public void handleCommand(CommandParameters args) {

        if (args.player.getAccessLevel() != AccessLevel.CREATOR){
            args.player.setAccessLevel(AccessLevel.CREATOR);
            accessLevel = AccessLevel.CREATOR;
            args.channel.sendMessage("you are now the creator").queue();
        }else{
            args.channel.sendMessage("you are already the creator").queue();
        }
    }
}
