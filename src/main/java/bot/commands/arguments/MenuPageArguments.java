package bot.commands.arguments;

import org.kohsuke.args4j.Argument;

public class MenuPageArguments {
    @Argument(metaVar = "page number", usage = "number of page to open in menu")
    public int pageNumber = 1;
}
