package bot.commands.user.shop;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;

public class ArmorShopCommand extends AbstractCommand<ShopPageArguments> {

    public ArmorShopCommand(AnimeCardsGame game) {
        super(game, ShopPageArguments.class);
        name = "armorshop";
        aliases = new String[]{"as"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        Paginator shop = game.getArmorShopViewer(event.getAuthor());
        shop.paginate(event.getChannel(), commandArgs.pageNumber);
    }
}
