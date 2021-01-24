package bot.commands.user.trading;

import bot.commands.AbstractCommand;
import bot.commands.arguments.RequiredPlayerArguments;
import bot.menu.MultiTradeContractMenu;
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
        Player targetPlayer = game.getCardPersonalOwner(commandArgs.id);
        if (targetPlayer == null){
            sendMessage(event, "cannot find player with id " + commandArgs.id);
            return;
        }

        MultiTradeContract contract = new MultiTradeContract(player.getId(), targetPlayer.getId());
        MultiTradeContractMenu menu = new MultiTradeContractMenu(game, contract);
        menu.sendMenu(event);
    }
}
