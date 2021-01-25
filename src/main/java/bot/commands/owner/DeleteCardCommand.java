package bot.commands.owner;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class DeleteCardCommand extends AbstractCommand<DeleteCardCommand.Arguments> {

    public static class Arguments{
        @Argument(metaVar = "card name", usage = "specify name or parts of name in quotes\nif it has multiple words (\" \")")
        String name;

        @Argument(metaVar = "card series", index = 1, usage = "also must be specified in quotes\nif it has multiple words (\" \")")
        String series;

        @Option(metaVar = "card id", name = "-id", usage = "optional card id to delete (overrides name and series)")
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
                sendMessage(event, String.format(
                        "card with unique\nname: %s\nseries: %s\nwas not found",
                        commandArgs.name, commandArgs.series
                ));
                return;
            }
        }else{
            card = game.getCardsGlobal().getById(commandArgs.cardId);
        }

        if (card != null){
            game.removeCard(card);
            sendMessage(event, String.format("card \"%s\" was deleted", card.getCharacterInfo().getFullName()));
        }else{
            sendMessage(event, String.format("card with id: %s was not found", commandArgs.cardId));
        }
    }
}
