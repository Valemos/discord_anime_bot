package bot.commands.user.stocks;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleIdentifiersArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardPersonal;

public class ExchangeForStockCommand extends AbstractCommand<MultipleIdentifiersArguments> {

    public ExchangeForStockCommand(AnimeCardsGame game) {
        super(game, MultipleIdentifiersArguments.class);
        name = "exchange";
        aliases = new String[]{"e"};
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {

        StringBuilder messageBuilder = new StringBuilder();
        for (String cardId : commandArgs.multipleIds){
            CardPersonal card = game.getCardsPersonal().getById(cardId);
            if(card != null && card.getOwner().equals(player)){

                float seriesStockValue = game.exchangeCardForStock(card);

                messageBuilder
                        .append(card.getCharacterInfo().getFullName())
                        .append(" exchanged for ")
                        .append(seriesStockValue)
                        .append('\n');
            }else{
                messageBuilder.append("you don't have card with id").append(cardId).append('\n');
            }
        }

        sendMessage(event, messageBuilder.toString());
    }
}
