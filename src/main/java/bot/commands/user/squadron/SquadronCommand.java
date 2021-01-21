package bot.commands.user.squadron;

import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
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
    public void handle(CommandEvent event) {
        Squadron squadron = game.getOrCreateSquadron(player);
        if (!squadron.isEmpty()){
            String squadronMessage = squadron.getSortedMembers().stream()
                    .map(member -> member.getCard().getNameStats() + " / "
                                + member.getHealthState().name().toLowerCase())
                    .collect(Collectors.joining("\n"));
            sendMessage(event, squadronMessage);
        }else{
            sendMessage(event, "squadron is empty");
        }
    }
}
