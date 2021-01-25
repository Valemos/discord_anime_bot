package bot.commands.user.trading;

import bot.commands.AbstractCommand;
import bot.menu.CardForCardContractMenu;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardPersonal;
import game.contract.CardForCardContract;
import org.kohsuke.args4j.Argument;

public class TradeCardForCardCommand extends AbstractCommand<TradeCardForCardCommand.Arguments> {

    public static class Arguments{
        @Argument(metaVar = "card id", usage = "from your collection", required = true)
        String sendCardId;

        @Argument(index = 1, metaVar = "receive card id", usage = "card owner will be notified about this contract", required = true)
        String receiveCardId;
    }

    public TradeCardForCardCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "tradecards";
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {
        Player targetPlayer = game.getCardPersonalOwner(commandArgs.receiveCardId);
        if (targetPlayer == null || targetPlayer.equals(player)){
            sendMessage(event, "cannot trade with player " + commandArgs.receiveCardId);
            return;
        }

        CardPersonal sendCard = game.getCardsPersonal().getById(commandArgs.sendCardId);
        CardPersonal receiveCard = game.getCardsPersonal().getById(commandArgs.receiveCardId);

        if (sendCard == null || !sendCard.getOwner().equals(player)){
            sendMessage(event, "your card id is incorrect");
            return;
        }else if (receiveCard == null || !receiveCard.getOwner().equals(targetPlayer)){
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

        contract.buildMenu(game).sendMenu(event);
    }
}
