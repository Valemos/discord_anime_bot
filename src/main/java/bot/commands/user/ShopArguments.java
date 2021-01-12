package bot.commands.user;

import org.kohsuke.args4j.Argument;

public class ShopArguments {
    @Argument(usage = "page number to show in shop")
    int pageNumber = 1;
}
