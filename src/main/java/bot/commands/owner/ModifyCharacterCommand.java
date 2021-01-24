package bot.commands.owner;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

public class ModifyCharacterCommand extends AbstractCommand<ModifyCharacterCommand.Arguments> {

    public static class Arguments{
        @Argument(metaVar = "card id", usage = "id of card to modify", required = true)
        String cardId;

        @Option(name = "-name", aliases = {"-n"}, metaVar = "name", usage = "new character")
        String newName;

        @Option(name = "-series", aliases = {"-s"}, metaVar = "series name", usage = "new or existing series")
        String newSeries;

        @Option(name = "-url", aliases = {"-u"}, metaVar = "image url", usage = "set image url")
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
