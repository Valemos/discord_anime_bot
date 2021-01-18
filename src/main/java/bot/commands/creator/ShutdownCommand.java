package bot.commands.creator;

import bot.BotAnimeCards;
import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;

public class ShutdownCommand extends AbstractCommandNoArguments {
    private final BotAnimeCards bot;

    public ShutdownCommand(BotAnimeCards bot) {
        super(bot.getGame());
        this.bot = bot;
        name = "shutdown";
    }

    @Override
    public void handle(CommandEvent event) {
        sendMessage(event, "bot is shutting down");
        bot.shutdown();
    }
}
