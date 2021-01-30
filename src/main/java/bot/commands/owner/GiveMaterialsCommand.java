package bot.commands.owner;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.materials.Material;
import org.kohsuke.args4j.Argument;

public class GiveMaterialsCommand extends AbstractCommand<GiveMaterialsCommand.Arguments> {

    public static class Arguments{
        @Argument(metaVar = "materials amount", required = true)
        int materialAmount;
    }

    public GiveMaterialsCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "givematerials";
        ownerCommand = true;
        hidden = true;
    }

    @Override
    public void handle(CommandEvent event) {
        for (Material m : Material.values()){
            player.getMaterials().addAmount(m, commandArgs.materialAmount);
        }

        sendMessage(event, "added " + commandArgs.materialAmount + " to all materials");
    }
}
