package bot.commands.user;

import bot.commands.AbstractCommand;
import bot.commands.arguments.RequiredPlayerArguments;
import bot.menu.MultiTradeContractMenu;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.contract.MultiTradeContract;

public class MultiTradeCommand extends AbstractCommand<RequiredPlayerArguments> {

    public MultiTradeCommand(AnimeCardsGame game) {
        super(game, RequiredPlayerArguments.class);
        name = "multiTrade";
        aliases = new String[]{"mt"};
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {
        String targetPlayerId = game.getCardPersonalOwner(commandArgs.id);
        if (!game.isPlayerExists(targetPlayerId)){
            sendMessage(event, "cannot find player with id " + targetPlayerId);
            return;
        }

        MultiTradeContract contract = new MultiTradeContract(player.getId(), targetPlayerId);
        MultiTradeContractMenu menu = new MultiTradeContractMenu(game, contract);
        menu.sendMenu(event);
    }
}
