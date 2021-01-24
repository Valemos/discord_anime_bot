package bot.commands.user.inventory;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import net.dv8tion.jda.api.EmbedBuilder;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.util.List;

public class InspectCardCommand extends AbstractCommand<InspectCardCommand.Arguments> {

    public static class Arguments {
        @Argument(required = true, metaVar = "card name", usage = "character name. can be multiple words without quotes")
        private List<String> cardName;

        @Option(name = "-s", metaVar = "series name", usage = "search for series. Must be in quotes if it has multiple words (\" \")")
        private String seriesName;

        public String getCardName() {
            if (cardName == null){
                return null;
            }

            return String.join(" ", cardName);
        }

    }

    public InspectCardCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "inspect";
        aliases = new String[]{"i"};
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        CardGlobal card = game.getCardGlobalUnique(commandArgs.getCardName(), commandArgs.seriesName);
        if (card != null){
            EmbedBuilder b = new EmbedBuilder();
            b.addField("Card info", card.getNameStats(), true);
            b.setThumbnail(card.getCharacterInfo().getImageUrl());
            event.getChannel().sendMessage(b.build()).queue();
        }else{
            event.getChannel().sendMessage("card not found").queue();
        }
    }
}
