package bot.menu;

import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.contract.CardForCardContract;

public class CardForCardContractMenu extends AbstractContractMenu<CardForCardContract> {

    public CardForCardContractMenu(AnimeCardsGame game, CardForCardContract contract) {
        super(game, CardForCardContract.class, contract);
    }

    @Override
    public void sendMenu(CommandEvent event) {

        String description =
                "Card of player " + contract.getSenderId() + '\n' +
                contract.getCardSending().getIdNameStats() + '\n' +
                "Card of player " + contract.getRecipientId() + '\n' +
                contract.getCardReceiving().getIdNameStats();

        EventHandlerButtonMenu menu = buildMenu(event, "Exchange cards", description);
        displayMenu(event.getChannel(), menu);
    }
}
