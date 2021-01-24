package bot.commands.owner;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import org.kohsuke.args4j.Argument;

public class ModifyCharacterCommand extends AbstractCommand<ModifyCharacterCommand.Arguments> {

    public static class Arguments{
        @Argument(metaVar = "card id", usage = "id of card to modify", required = true)
        String cardId;

        @Argument(index = 1, metaVar = "new character name", required = true)
        String newName;

        @Argument(index = 2, metaVar = "new series name", required = true)
        String newSeries;

        @Argument(index = 3, metaVar = "new image url", required = true)
        String newImageUrl;
    }

    public ModifyCharacterCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "modifycard";
        aliases = new String[]{"modcard", "mod"};
        ownerCommand = true;
    }

    @Override
    public void handle(CommandEvent event) {
        CardGlobal card = game.getCardsGlobal().getById(commandArgs.cardId);
        if (card != null){
            game.getCardsGlobal().updateCharacterInfo(card.getCharacterInfo(),
                                                        commandArgs.newName,
                                                        commandArgs.newSeries,
                                                        commandArgs.newImageUrl);

        }else{
            sendMessage(event, "card with id " + commandArgs.cardId + " not found");
        }
    }
}
