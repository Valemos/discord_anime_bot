package bot.commands.user;

import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.items.Material;

import java.util.Random;

public class DailyCommand extends AbstractCommandNoArguments {
    private final int inverseProbability;

    public DailyCommand(AnimeCardsGame game) {
        super(game);
        name = "daily";
        guildOnly = false;
        inverseProbability = 1000;
    }

    @Override
    public void handle(CommandEvent event) {
        int goldReceived = getRandomGold();
        player.getMaterials().addAmount(Material.GOLD, goldReceived);
        sendMessage(event, "you received " + goldReceived + " gold!");
    }

    private int getRandomGold() {
        if (getRandomInt(inverseProbability) == 0){
            return 200;
        }

        return 50 + getRandomInt(50);
    }

    private int getRandomInt(int max) {
        return new Random().nextInt(max);
    }
}
