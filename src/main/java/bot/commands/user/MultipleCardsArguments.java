package bot.commands.user;

import org.kohsuke.args4j.Argument;

import java.util.List;

public class MultipleCardsArguments {
    @Argument(usage = "card ids you want to add to squadron")
    List<String> cardIds;
}
