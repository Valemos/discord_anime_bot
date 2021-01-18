package bot.commands.owner;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import org.kohsuke.args4j.Argument;

public class AddCardCommand extends AbstractCommand<AddCardCommand.Args> {

    public static class Args{
        @Argument(metaVar = "name", usage = "name of character", required = true)
        String name;

        @Argument(metaVar = "series", index = 1, usage = "series where the character from", required = true)
        String series;

        @Argument(metaVar = "image url", index = 2, usage = "image url to use for card", required = true)
        String imageUrl;
    }

    public AddCardCommand(AnimeCardsGame game) {
        super(game, Args.class);
        name = "addcard";
        aliases = new String[]{"cr"};
        guildOnly = false;
        ownerCommand = true;
    }

    @Override
    public void handle(CommandEvent event) {
        CardGlobal new_card = new CardGlobal(
                commandArgs.name,
                commandArgs.series,
                commandArgs.imageUrl
        );

        game.addCard(new_card);

        sendMessage(event, "new card added, card id is " + new_card.getCharacterInfo().getId());
    }
}
