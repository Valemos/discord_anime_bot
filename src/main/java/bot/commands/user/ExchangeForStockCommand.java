package bot.commands.user;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardPersonal;

public class ExchangeForStockCommand extends AbstractCommand<MultipleCardsArguments> {

    public ExchangeForStockCommand(AnimeCardsGame game) {
        super(game, MultipleCardsArguments.class);
        name = "exchange";
        aliases = new String[]{"e"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {

        StringBuilder messageBuilder = new StringBuilder();
        for (String cardId : commandArgs.cardIds){
            CardPersonal card = game.getPersonalCard(player, cardId);
            if(card != null){
                float seriesStockValue = game.exchangeCardForStock(player, card);

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
