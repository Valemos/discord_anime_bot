package bot.commands.user;

import bot.CommandPermissions;
import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.shop.ShopViewer;
import org.kohsuke.args4j.Argument;

public class ShopCommand extends AbstractCommand<ShopCommand.Arguments> {

    public static class Arguments{
        @Argument(usage = "page number to show in shop")
        int pageNumber = 1;
    }

    public ShopCommand(AnimeCardsGame game) {
        super(game, Arguments.class, CommandPermissions.USER);
        name = "shop";
        aliases = new String[]{"s"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        game.getShopViewer(event.getAuthor()).paginate(event.getChannel(), commandArgs.pageNumber);
    }
}
