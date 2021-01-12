package bot.commands.user;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;

public class ArmorShopCommand extends AbstractCommand<ShopArguments> {

    public ArmorShopCommand(AnimeCardsGame game) {
        super(game, ShopArguments.class);
        name = "armorshop";
        aliases = new String[]{"as"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        Paginator shop = game.getArmorShopViewer(event.getAuthor());
        player.setShopViewer(shop);
        shop.paginate(event.getChannel(), commandArgs.pageNumber);
    }
}
