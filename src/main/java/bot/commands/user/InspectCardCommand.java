package bot.commands.user;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import net.dv8tion.jda.api.EmbedBuilder;
import org.kohsuke.args4j.Argument;

public class InspectCardCommand extends AbstractCommand<InspectCardCommand.Arguments> {

    public static class Arguments {
        @Argument(usage = "card name and series")
        String cardName;
    }

    public InspectCardCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "inspect";
        aliases = new String[]{"i"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        CardGlobal card = game.getGlobalCardByName(commandArgs.cardName);
        if (card != null){
            EmbedBuilder b = new EmbedBuilder();
            b.addField("Card info", card.getOneLineRepresentationString(), true);
            b.setImage(card.getCharacterInfo().getImageUrl());
            event.getChannel().sendMessage(b.build()).queue();
        }else{
            event.getChannel().sendMessage("card not found").queue();
        }
    }
}
