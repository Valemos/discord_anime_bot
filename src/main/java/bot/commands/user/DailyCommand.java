package bot.commands.user;

import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.materials.Material;

import java.util.Random;

public class DailyCommand extends AbstractCommandNoArguments {
    public final int rareProbability;
    public final int rareReward;
    public final int baseReward;
    public final int rewardRandomIncrement;

    public DailyCommand(AnimeCardsGame game) {
        super(game);
        name = "daily";
        guildOnly = false;

        baseReward = 50;
        rewardRandomIncrement = 50;
        rareReward = 200;
        rareProbability = 1000;
    }

    @Override
    public void handle(CommandEvent event) {
        int goldReceived = getRandomGold();
        player.getMaterials().addAmount(Material.GOLD, goldReceived);
        sendMessage(event, "you received " + goldReceived + " gold!");
    }

    public int getRareProbability() {
        return rareProbability;
    }

    public int getRandomGold() {
        Random random = new Random();
        if (getRandomInt(rareProbability, random) == 0){
            return rareReward;
        }

        return baseReward + getRandomInt(rewardRandomIncrement, random);
    }

    private int getRandomInt(int max, Random random) {
        return random.nextInt(max);
    }
}
