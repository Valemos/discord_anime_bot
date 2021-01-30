package bot.commands.arguments;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.util.ArrayList;
import java.util.List;

public class CardSelectionArguments {
    @Argument(metaVar = "card name", usage = "character name. can be multiple words without quotes")
    public List<String> cardNameWords = new ArrayList<>();

    @Option(name = "-s", metaVar = "series name", usage = "search for series. Must be in quotes if it has multiple words (\" \")")
    public String seriesName;

    @Option(name = "-id", metaVar = "card id", usage = "search global card by specific id")
    public String cardId;

    public String getCardName() {
        if (cardNameWords.isEmpty()) {
            return null;
        }

        return String.join(" ", cardNameWords);
    }

}
