package bot.commands.user;

import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Material;

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
    protected void handle(CommandEvent event) {
        int goldReceived = getRandomGold();
        player.getMaterials().incrementAmount(Material.GOLD, 100);
        sendMessage(event, String.format("you received %s gold!", goldReceived));
    }

    private int getRandomGold() {
        if (getRandomInt(inverseProbability) == 0){
            return 200;
        }

        return 50 + getRandomInt(50);
    }

    private int getRandomInt(int max) {
        return new Random(max).nextInt();
    }
}
