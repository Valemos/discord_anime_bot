package bot.commands.creator;

import bot.CommandPermissions;
import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import org.kohsuke.args4j.Argument;

public class DeleteItemCommand extends AbstractCommand<DeleteItemCommand.Arguments> {

    public static class Arguments{
        @Argument(usage = "item id specified in all items list", required = true)
        String itemId;
    }

    public DeleteItemCommand(AnimeCardsGame game) {
        super(game, Arguments.class, CommandPermissions.CREATOR);
        name = "itemdelete";
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
