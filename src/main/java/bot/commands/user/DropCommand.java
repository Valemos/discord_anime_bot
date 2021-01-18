package bot.commands.user;


import bot.CommandPermissions;
import bot.commands.AbstractCommandNoArguments;
import bot.menu.EmojiMenuHandler;
import bot.menu.BotMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardDropActivity;
import game.cooldown.CooldownSet;
import game.Player;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.cards.CardsGlobalManager;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.time.Instant;
import java.util.List;

public class DropCommand extends AbstractCommandNoArguments {

    public DropCommand(AnimeCardsGame game) {
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
        cardDrop.showMenu(event.getChannel(), game);
    }
}