package bot.commands.creator;

import bot.CommandAccessLevel;
import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import org.kohsuke.args4j.Argument;

public class DeleteItemCommand extends AbstractCommand<DeleteItemCommand.Arguments> {

    static class Arguments{
        @Argument(usage = "item id specified in all items list", required = true)
        String itemId;
    }

    public DeleteItemCommand(AnimeCardsGame game) {
        super(game, Arguments.class, CommandAccessLevel.CREATOR);
    }

    @Override
    protected void handle(CommandEvent event) {
        if (game.removeItemById(commandArgs.itemId)){
            sendMessage(event, "item deleted");
        }else{
            sendMessage(event, "item not found");
        }

    }
}
