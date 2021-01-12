package bot.commands.creator;

import bot.CommandAccessLevel;
import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.items.ItemGlobal;
import org.kohsuke.args4j.Argument;

public class AddItemCommand extends AbstractCommand<AddItemCommand.Arguments>{

    public static class Arguments{

        @Argument(required = true)
        String name;

        @Argument(index = 1, usage = "attack stat for item", required = true)
        float attack;

        @Argument(index = 2, usage = "defense stat for item", required = true)
        float defense;

        @Argument(index = 3)
        String description = "";
    }

    public AddItemCommand(AnimeCardsGame game) {
        super(game, Arguments.class, CommandAccessLevel.CREATOR);
    }

    @Override
    protected void handle(CommandEvent event) {
        ItemGlobal newItem = game.addItem(new ItemGlobal(
                commandArgs.name,
                commandArgs.attack,
                commandArgs.defense,
                commandArgs.description
        ));

        event.getChannel().sendMessage(
                String.format("new item \"%s\" created with id %s", newItem.getName(), newItem.getId())
        ).queue();
    }
}
