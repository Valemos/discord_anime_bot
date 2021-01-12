package bot.commands.creator;

import bot.CommandPermissions;
import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Material;
import game.items.MaterialsSet;
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

        @Argument(index = 3, usage="price for this item", required = true)
        int goldPrice;

        @Argument(index = 4)
        String description = "";
    }

    public AddItemCommand(AnimeCardsGame game) {
        super(game, Arguments.class, CommandPermissions.CREATOR);
        name = "itemadd";
    }

    @Override
    protected void handle(CommandEvent event) {

        MaterialsSet newMaterialsSet = new MaterialsSet();
        newMaterialsSet.setAmount(Material.GOLD, commandArgs.goldPrice);

        ItemGlobal newItem = game.addItem(new ItemGlobal(
                commandArgs.name,
                commandArgs.attack,
                commandArgs.defense,
                commandArgs.description,
                newMaterialsSet));

        event.getChannel().sendMessage(
                String.format("new item \"%s\" created with id %s", newItem.getName(), newItem.getStringId())
        ).queue();
    }
}
