package bot.commands.user.trading;

import bot.commands.AbstractCommand;
import bot.commands.arguments.RequiredPlayerArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.contract.MultiTradeContract;

public class MultiTradeCommand extends AbstractCommand<RequiredPlayerArguments> {

    public MultiTradeCommand(AnimeCardsGame game) {
        super(game, RequiredPlayerArguments.class);
        name = "multitrade";
        aliases = new String[]{"mt"};
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {
        Player targetPlayer = game.getPlayer(commandArgs.id);
        if (targetPlayer == null || targetPlayer.equals(player)){
            sendMessage(event, "cannot trade with player " + commandArgs.id);
            return;
        }

        MultiTradeContract contract = new MultiTradeContract(player.getId(), targetPlayer.getId());
        contract.buildMenu(game).sendMenu(event);
    }
}
