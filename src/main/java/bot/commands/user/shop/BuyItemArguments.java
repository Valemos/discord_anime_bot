package bot.commands.user.shop;

import org.kohsuke.args4j.Argument;

public class BuyItemArguments {
    @Argument(metaVar = "item id or name", required = true)
    String itemId;
}
