package bot.commands.arguments;

import org.kohsuke.args4j.Argument;

import java.util.List;

public class MultipleIdentifiersArguments {
    @Argument(usage = "ids you want to use in command")
    public List<String> multipleIds;
}
