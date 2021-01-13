package bot.commands.user;

import bot.CommandPermissions;
import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;

public class ShopCommand extends AbstractCommand<ShopArguments> {

    public ShopCommand(AnimeCardsGame game) {
        super(game, ShopArguments.class, CommandPermissions.USER);
        name = "shop";
        aliases = new String[]{"s"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        Paginator shop = game.getItemShopViewer(event.getAuthor());
        shop.paginate(event.getChannel(), commandArgs.pageNumber);
    }
}
