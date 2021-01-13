package bot.commands;

import org.kohsuke.args4j.Argument;

import java.util.List;

public class MultipleWordsArguments {
    @Argument(required = true, usage = "enter one or multiple words")
    public List<String> multipleWords;


    public String getSingleString() {
        return String.join(" ", multipleWords);
    }
}
