package bot.commands.user.shop;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MenuPageArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;

public class ShopCommand extends AbstractCommand<MenuPageArguments> {

    public ShopCommand(AnimeCardsGame game) {
        super(game, MenuPageArguments.class);
        name = "shop";
        aliases = new String[]{"s"};
        guildOnly = false;
        help = "shows power up items shop. can be provided with page id for quick navigation (for multiple pages)";
    }

    @Override
    public void handle(CommandEvent event) {
        Paginator shop = game.getItemShopViewer(event.getAuthor());
        shop.paginate(event.getChannel(), commandArgs.pageNumber);
    }
}
