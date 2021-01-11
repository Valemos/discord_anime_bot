package bot.commands.creator;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardStatsGlobal;
import game.cards.CharacterCardGlobal;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.EnumOptionHandler;

public class CreateGlobalCardCommand extends AbstractCommand<CreateGlobalCardCommand.Args> {

    public enum State{
        NAME("name"),
        SERIES("series");

        private String name;

        State(String name) {
            this.name = name;
        }
    }

    public static class Args{

        @Option(name = "-s", handler = EnumOptionHandler.class)
        State state = null;

//        @Argument(index = 0, required = true)
//        String name;
//
//        @Argument(index = 1, required = true)
//        String series;
//
//        @Argument(index = 2)
//        String imageUrl = null;
    }

    public CreateGlobalCardCommand(AnimeCardsGame game) {
        super(game, Args.class);
        name = "createcard";
        aliases = new String[]{"cr"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
//        CharacterCardGlobal new_card = new CharacterCardGlobal(
//                argumentsObject.name,
//                argumentsObject.series,
//                argumentsObject.imageUrl,
//                new CardStatsGlobal());
//
//        game.addCard(new_card);
//        event.getChannel().sendMessage("new card added, card id is " + new_card.getCharacterInfo().getId()).queue();
    }
}
