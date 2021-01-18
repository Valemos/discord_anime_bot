package bot.commands.user.carddrop;


import bot.CommandPermissions;
import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardDropActivity;
import game.cooldown.CooldownSet;
import game.cards.CardGlobal;
import game.cards.CardsGlobalManager;

import java.time.Instant;
import java.util.List;

public class CardDropCommand extends AbstractCommandNoArguments {

    public CardDropCommand(AnimeCardsGame game) {
        super(game, CommandPermissions.USER);
        name = "drop";
    }

    @Override
    public void handle(CommandEvent event) {
        Instant now = Instant.now();

        CooldownSet cooldowns = game.getCooldowns(player.getId());
        if (!cooldowns.getDrop().tryUse(now)){
            sendMessage(event, cooldowns.getDrop().getVerboseDescription(now));
            return;
        }

        CardsGlobalManager collection = game.getCardsGlobalManager();
        List<CardGlobal> cards = collection.getRandomCards(3);


        CardDropActivity cardDrop = new CardDropActivity(cards);
        cardDrop.createMenu(event.getChannel(), game);
    }
}