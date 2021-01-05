package bot.commands;

import game.AnimeCardsGame;
import game.Player;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandArguments {
    MessageChannel channel;
    Player player;
    AnimeCardsGame game;
    String[] commandArgs;

    public CommandArguments(AnimeCardsGame game, Player player, MessageChannel channel, String[] commandArgs) {
        this.channel = channel;
        this.player = player;
        this.game = game;
        this.commandArgs = commandArgs;
    }
}
