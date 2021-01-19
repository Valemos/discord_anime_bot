package bot.commands.user;

import bot.commands.AbstractCommand;
import bot.menu.CardForCardContractMenu;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardPersonal;
import game.contract.CardForCardContract;
import org.kohsuke.args4j.Argument;

public class TradeCommand extends AbstractCommand<TradeCommand.Arguments> {

    public static class Arguments{
        @Argument(metaVar = "send card id", required = true)
        String sendCardId;

        @Argument(index = 1, metaVar = "receive card id", required = true)
        String receiveCardId;
    }

    public TradeCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "trade";
    }

    @Override
    public void handle(CommandEvent event) {
        Player targetPlayer = game.getCardPersonalOwner(commandArgs.receiveCardId);
        if (targetPlayer == null){
            sendMessage(event, "cannot find card owner for " + commandArgs.receiveCardId);
            return;
        }

        CardPersonal sendCard = game.getCardsPersonal().getById(commandArgs.sendCardId);
        CardPersonal receiveCard = game.getCardsPersonal().getById(commandArgs.receiveCardId);

        if (sendCard == null){
            sendMessage(event, "your card id is incorrect");
            return;
        }else if (receiveCard == null){
            sendMessage(event, "other card id is incorrect");
            return;
        }

        if (player.equals(receiveCard.getOwner())){
            sendMessage(event, "you already own this card");
            return;
        }

        CardForCardContract contract = new CardForCardContract(
                player.getId(),
                targetPlayer.getId(),
                sendCard,
                receiveCard
        );

        CardForCardContractMenu menu = new CardForCardContractMenu(game, contract);
        menu.sendMenu(event);
    }
}
