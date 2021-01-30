package bot.commands.user.inventory;

import bot.commands.AbstractCommand;
import bot.commands.arguments.CardSelectionArguments;
import bot.commands.owner.CardSelectionCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import net.dv8tion.jda.api.EmbedBuilder;

public class InspectCardCommand extends CardSelectionCommand {

    public InspectCardCommand(AnimeCardsGame game) {
        super(game, CardSelectionArguments.class);
        name = "inspect";
        aliases = new String[]{"i"};
        guildOnly = false;
    }

    @Override
    public void handleCard(CommandEvent event, CardGlobal card) {
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
