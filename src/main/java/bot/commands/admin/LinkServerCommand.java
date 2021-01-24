package bot.commands.admin;

import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class LinkServerCommand extends AbstractCommandNoArguments {

    public LinkServerCommand(AnimeCardsGame game) {
        super(game);
        name = "link";
    }

    @Override
    public void handle(CommandEvent event) {

    }
}
