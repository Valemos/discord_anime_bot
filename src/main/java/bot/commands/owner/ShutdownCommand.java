package bot.commands.owner;

import bot.BotAnimeCards;
import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShutdownCommand extends AbstractCommandNoArguments {
    private final BotAnimeCards bot;

    public ShutdownCommand(BotAnimeCards bot) {
        super(bot.getGame());
        this.bot = bot;
        name = "shutdown";
        ownerCommand = true;
        help = "shuts down current running instance of the bot (waits until running processes will finish)";
    }

    @Override
    public void handle(CommandEvent event) {
        sendMessage(event, "bot is shutting down");
        Logger.getGlobal().log(Level.INFO, "user " + event.getAuthor().getName() + " : " + event.getAuthor().getId() + " shut down the bot");
        bot.shutdown();
    }
}
