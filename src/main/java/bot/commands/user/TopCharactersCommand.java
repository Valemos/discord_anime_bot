package bot.commands.user;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MenuPageArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class TopCharactersCommand extends AbstractCommand<MenuPageArguments> {

    public TopCharactersCommand(AnimeCardsGame game) {
        super(game, MenuPageArguments.class);
        name = "top";
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {

    }
}
