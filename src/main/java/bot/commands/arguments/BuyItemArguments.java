package bot.commands.arguments;

import org.kohsuke.args4j.Argument;

public class BuyItemArguments {
    @Argument(metaVar = "item id or name", required = true)
    public String itemId;
}
