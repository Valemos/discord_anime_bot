package bot.commands.owner;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class DeleteCardCommand extends AbstractCommand<DeleteCardCommand.Arguments> {

    public static class Arguments{
        @Argument(metaVar = "card name", usage = "specify name or parts of name of character card")
        String name;

        @Argument(metaVar = "card series", index = 1, usage = "specify series name for card")
        String series;

        @Option(metaVar = "card identifier", name = "-id", usage = "card Id to delete")
        String cardId;
    }

    public DeleteCardCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "delcard";
        ownerCommand = true;
    }

    @Override
    public void handle(CommandEvent event) {
        CardGlobal card;
        if (commandArgs.cardId == null){
            if(commandArgs.name == null){
                sendMessage(event, "incorrect options, specify name and/or series or id");
                return;
            }

            card = game.getCardGlobalUnique(commandArgs.name, commandArgs.series);

            if (card == null){
                sendMessage(event,
                        String.format(
                                "card with unique\nname: %s\nseries: %s\nwas not found", commandArgs.name, commandArgs.series
                        ));
                return;
            }
        }
        else{
            card = game.getCardGlobalById(commandArgs.cardId);
        }

        if (card != null){
            String cardName = card.getCharacterInfo().getFullName();
            game.removeCardById(card.getId());
            sendMessage(event, String.format("card \"%s\" was deleted", cardName));
        }else{
            sendMessage(event, String.format("card with unique id: %s was not found", commandArgs.cardId));
        }
    }
}
