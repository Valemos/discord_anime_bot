package bot.commands.owner;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import org.kohsuke.args4j.Argument;

public class AddCardCommand extends AbstractCommand<AddCardCommand.Arguments> {

    public static class Arguments {
        @Argument(metaVar = "name", usage = "new character name", required = true)
        public String name;

        @Argument(metaVar = "series", index = 1, usage = "where the character from", required = true)
        public String series;

        @Argument(metaVar = "image url", index = 2, usage = "from this url image will be loaded to discord", required = true)
        public String imageUrl;
    }

    public AddCardCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "createcard";
        aliases = new String[]{"cr"};
        guildOnly = false;
        ownerCommand = true;
    }

    @Override
    public void handle(CommandEvent event) {
        CardGlobal newCard = new CardGlobal(
                commandArgs.name,
                commandArgs.series,
                commandArgs.imageUrl
        );

        if (game.addCard(newCard)){
            sendMessage(event, "new card added, card id is `" + newCard.getCharacterInfo().getId() + '`');
        }else{
            sendMessage(event, "cannot add card with this data");
        }
    }
}
