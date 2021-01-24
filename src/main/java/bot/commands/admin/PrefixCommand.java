package bot.commands.admin;

import bot.BotAnimeCards;
import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.kohsuke.args4j.Argument;

public class PrefixCommand extends AbstractCommand<PrefixCommand.Arguments> {
    private final BotAnimeCards bot;

    public static class Arguments{
        @Argument(metaVar = "new prefix", required=true, usage = "will be set to this bot as alternative to default prefix")
        String prefix;
    }

    public PrefixCommand(BotAnimeCards bot) {
        super(bot.getGame(), Arguments.class);
        this.bot = bot;
        name = "prefix";
        requiredRole = "admin";
    }

    @Override
    public void handle(CommandEvent event) {
        bot.rebuildCommandClient(commandArgs.prefix);
        sendMessage(event, "new prefix \"" + commandArgs.prefix +"\" is set.\n but you can still use default prefix");
    }

}
