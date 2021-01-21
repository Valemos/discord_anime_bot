package bot.commands.user.squadron;

import bot.commands.AbstractCommand;
import bot.commands.arguments.MultipleIdentifiersArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.squadron.Squadron;

public class SquadronRemoveCommand extends AbstractCommand<MultipleIdentifiersArguments> {
    public SquadronRemoveCommand(AnimeCardsGame game) {
        super(game, MultipleIdentifiersArguments.class);
        name = "squadronremove";
        aliases = new String[]{"sqr"};
        guildOnly = false;
    }

    @Override
    public void handle(CommandEvent event) {
        Squadron squadron = game.getOrCreateSquadron(player);

        if (squadron.getMembers().removeIf(
                (member) -> commandArgs.multipleIds.contains(member.getCard().getId())
        )){
            sendMessage(event, "removed cards");
        }else{
            sendMessage(event, "cannot remove any card");
        }
    }
}
