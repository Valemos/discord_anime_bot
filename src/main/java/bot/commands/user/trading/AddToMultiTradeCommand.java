package bot.commands.user.trading;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;

public class AddToMultiTradeCommand extends AbstractCommand<AddToMultiTradeCommand.Arguments> {

    public static class Arguments {
    }

    public AddToMultiTradeCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "addtrade";
        aliases = new String[]{"trade"};
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {

    }
}
