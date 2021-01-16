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
        @Argument(required = true, metaVar = "card name", usage = "name of card character")
        private List<String> cardName;

        @Option(name = "-s", metaVar = "series name", usage = "series to search for")
        private List<String> seriesName;

        public String getCardName() {
            if (cardName == null){
                return null;
            }

            return String.join(" ", cardName);
        }

        public String getSeriesName() {
            if (seriesName == null){
                return null;
            }

            return String.join(" ", seriesName);
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
        CardGlobal card = game.getCardGlobal(commandArgs.getCardName(), commandArgs.getSeriesName());
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
