package bot.commands.creator;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import org.kohsuke.args4j.Argument;

public class DeleteCardCommand extends AbstractCommand<DeleteCardCommand.Arguments> {

    public static class Arguments{
        @Argument(usage = "specify name or parts of name of character card")
        String name;

        @Argument(index = 1, usage = "specify series name for card")
        String series;

        @Argument(usage = "specify card Id")
        String cardId;
    }

    public DeleteCardCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "delcard";
        ownerCommand = true;
    }

    @Override
    protected void handle(CommandEvent event) {
        CardGlobal card;
        if (commandArgs.cardId == null){
            card = game.getCardGlobal(commandArgs.name, commandArgs.series);
        }
        else{
            card = game.getCardGlobalById(commandArgs.cardId);
        }

        if (card != null){
            String cardName = card.getCharacterInfo().getFullName();
            game.removeCardById(card.getId());
            sendMessage(event, String.format("card \"%s\" was deleted", cardName));
        }else{
            sendMessage(event,
                    String.format(
                            "card with\nname: %s\nseries: %s\nid: %s\nwas not found",
                            commandArgs.name, commandArgs.series, commandArgs.cardId)
            );
        }
    }
}
