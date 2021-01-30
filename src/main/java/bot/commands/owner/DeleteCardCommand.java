package bot.commands.owner;

import bot.commands.arguments.CardSelectionArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;

public class DeleteCardCommand extends CardSelectionCommand {

    public DeleteCardCommand(AnimeCardsGame game) {
        super(game, CardSelectionArguments.class);
        name = "delcard";
        ownerCommand = true;
    }

    @Override
    public void handleCard(CommandEvent event, CardGlobal card) {
        if (card != null){
            game.removeCard(card);
            sendMessage(event, String.format("card \"%s\" was deleted", card.getCharacterInfo().getFullName()));
        }else{
            sendMessage(event, String.format("card with id: %s was not found", commandArgs.cardId));
        }
    }
}
