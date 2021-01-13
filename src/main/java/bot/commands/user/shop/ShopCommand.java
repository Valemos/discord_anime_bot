package bot.commands.user.shop;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;

public class ShopCommand extends AbstractCommand<ShopPageArguments> {

    public ShopCommand(AnimeCardsGame game) {
        super(game, ShopPageArguments.class);
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
