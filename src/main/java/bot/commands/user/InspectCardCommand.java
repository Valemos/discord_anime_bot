package bot.commands.user;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CharacterCardGlobal;
import net.dv8tion.jda.api.EmbedBuilder;

public class InspectCardCommand extends AbstractCommand<AbstractCommand.NoArgumentsConfig> {

    public InspectCardCommand(AnimeCardsGame game) {
        super(game, NoArgumentsConfig.class);
        name = "inspect";
        aliases = new String[]{"i"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        CharacterCardGlobal card = game.getGlobalCardById(1);
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
