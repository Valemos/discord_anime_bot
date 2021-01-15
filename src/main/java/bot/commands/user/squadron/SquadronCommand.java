package bot.commands.user.squadron;

import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardPersonal;
import game.squadron.Squadron;

import java.util.stream.Collectors;

public class SquadronCommand extends AbstractCommandNoArguments {

    public SquadronCommand(AnimeCardsGame game) {
        super(game);
        name = "squadron";
        aliases = new String[]{"sq"};
        guildOnly = false;
    }

    @Override
    protected void handle(CommandEvent event) {
        Squadron squadron = game.getSquadron(player);
        if (!squadron.isEmpty()){
            String squadronMessage = squadron.getSortedCards().stream()
                    .map(CardPersonal::getNameStats)
                    .collect(Collectors.joining("\n"));
            sendMessage(event, squadronMessage);
        }else{
            sendMessage(event, "squadron is empty");
        }
    }
}
