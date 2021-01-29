package bot.commands.user.trading;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardPersonal;
import game.contract.SendCardsContract;
import org.kohsuke.args4j.Argument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SendCardsCommand extends AbstractCommand<SendCardsCommand.Arguments> {

    public static class Arguments {
        @Argument(required = true, metaVar = "recipient id", usage = "id of player to send cards")
        String recieverId;

        @Argument(index = 1, required = true, metaVar = "cards id list", usage = "multiple card ids to send them to player")
        List<String> cardIds = new ArrayList<>();
    }

    public SendCardsCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "sendcards";
        aliases = new String[]{"sc"};
    }

    @Override
    public void handle(CommandEvent event) {
        Player targetPlayer = game.getPlayer(commandArgs.recieverId);

        if (targetPlayer == null || targetPlayer.equals(player)){
            sendMessage(event,
                    "cannot send cards to "
                    + commandArgs.recieverId
                    + "\ncheck if player id is correct");
            return;
        }

        List<CardPersonal> cardsSending = commandArgs.cardIds.stream()
                .map(cardId -> game.getCardsPersonal().getById(cardId))
                .filter(Objects::nonNull)
                .filter(card -> card.getOwner().equals(player))
                .collect(Collectors.toList());

        if(cardsSending.isEmpty()){
            sendMessage(event, "you provided not valid card ids");
            return;
        }

        SendCardsContract contract = new SendCardsContract(
                player.getId(),
                targetPlayer.getId(),
                cardsSending
        );

        contract.buildMenu(game).sendMenu(event);
    }
}
