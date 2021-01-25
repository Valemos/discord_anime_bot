package bot.menu;

import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardPersonal;
import game.contract.SendCardsContract;

import java.util.List;

public class SendCardsContractMenu extends AbstractContractMenu<SendCardsContract> {
    public SendCardsContractMenu(AnimeCardsGame game, SendCardsContract contract) {
        super(game, contract, "Send cards");
    }
}
