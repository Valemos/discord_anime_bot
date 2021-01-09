package bot.commands;

import bot.commands.arguments.MessageArguments;
import game.AnimeCardsGame;
import game.Player;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandParameters {
    public MessageChannel channel;
    public Player player;
    public AnimeCardsGame game;
    public User user;
    public MessageArguments messageArgs;

    public CommandParameters(AnimeCardsGame game, Player player, User user, MessageChannel channel, MessageArguments arguments) {
        this.user = user;
        this.channel = channel;
        this.player = player;
        this.game = game;
        this.messageArgs = arguments;
    }
}
