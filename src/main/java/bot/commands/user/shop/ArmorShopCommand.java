package bot.commands.user.shop;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MenuPageArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import game.AnimeCardsGame;

public class ArmorShopCommand extends AbstractCommand<MenuPageArguments> {

    public ArmorShopCommand(AnimeCardsGame game) {
        super(game, MenuPageArguments.class);
        name = "armorshop";
        aliases = new String[]{"as"};
        guildOnly = false;
        help = "shows armor shop list. can be provided with page id for quick navigation";
    }

    @Override
    public void handle(CommandEvent event) {
        Paginator shop = game.getArmorShopViewer(event.getAuthor());
        shop.paginate(event.getChannel(), commandArgs.pageNumber);
    }
}
