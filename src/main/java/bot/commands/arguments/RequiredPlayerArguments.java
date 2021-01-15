package bot.commands.arguments;

import org.kohsuke.args4j.Argument;

public class RequiredPlayerArguments {
    @Argument(required = true, metaVar = "other player id", usage = "id to use for command")
    public String id;


}
