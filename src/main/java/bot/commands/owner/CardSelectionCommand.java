package bot.commands.owner;

import bot.commands.AbstractCommand;
import bot.commands.arguments.CardSelectionArguments;
import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;

import java.util.List;

public abstract class CardSelectionCommand extends AbstractCommand<CardSelectionArguments> {

    public CardSelectionCommand(AnimeCardsGame game, Class<CardSelectionArguments> argumentsClass) {
        super(game, argumentsClass);
    }

    public abstract void handleCard(CommandEvent event, CardGlobal card);

    @Override
    public void handle(CommandEvent event) {
        CardGlobal card = null;

        if (commandArgs.cardId != null) {
            card = game.getCardsGlobal().getById(commandArgs.cardId);

        } else {
            if(commandArgs.cardNameWords.isEmpty() && commandArgs.seriesName == null){
                sendMessage(event, "incorrect options, specify name and/or series or id");
                return;
            }

            List<CardGlobal> cards = game.getCardsGlobal()
                    .getFiltered(commandArgs.getCardName(), commandArgs.seriesName);

            if (cards.isEmpty()){
                sendMessage(event, "card not found");
                return;
            }

            if (cards.size() > 1){
                sendMessage(event, "`multiple cards found. Select exact card with -id option");
                BotMenuCreator.menuForCardIds(cards, event, game, 1);
            }else{
                card = cards.get(0);
            }
        }

        if (card != null){
            handleCard(event, card);
        }else{
            sendMessage(event, "card not found");
        }
    }
}
