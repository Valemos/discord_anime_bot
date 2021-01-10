package bot.commands.handlers.user;

import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.AbstractCommand;
import bot.commands.parsing.ArgumentSettingsBuilder;
import bot.commands.parsing.IntegerArgument;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CharacterCardGlobal;
import net.dv8tion.jda.api.EmbedBuilder;

public class InspectCardCommand extends AbstractCommand {

    public InspectCardCommand(AnimeCardsGame game) {
        super(game);
        name = "inspect";
        aliases = new String[]{"i"};
        guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        int cardId = Integer.parseInt(parameters.messageArgs.get(0));

        CharacterCardGlobal card = game.getGlobalCardById(cardId);
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
