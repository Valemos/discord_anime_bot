package bot.commands.user.shop;

import org.kohsuke.args4j.Argument;

public class ShopPageArguments {
    @Argument(usage = "page number to show in shop")
    int pageNumber = 1;
}
