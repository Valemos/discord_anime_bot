package bot.commands;

import game.AnimeCardsGame;
import game.Player;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandParameters {
    MessageChannel channel;
    Player player;
    AnimeCardsGame game;
    MessageArguments messageArgs;

    public CommandParameters(AnimeCardsGame game, Player player, MessageChannel channel, MessageArguments arguments) {
        this.channel = channel;
        this.player = player;
        this.game = game;
        this.messageArgs = arguments;
    }
}
