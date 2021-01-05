package bot.commands;

import game.AnimeCardsGame;
import game.Player;

public class CommandArguments {
    Player player;
    AnimeCardsGame game;
    String[] arguments;

    public CommandArguments(Player player, AnimeCardsGame game, String[] arguments) {
        this.player = player;
        this.game = game;
        this.arguments = arguments;
    }
}
