package bot.commands.user;


import bot.CommandPermissions;
import bot.commands.AbstractCommandNoArguments;
import bot.menu.EmojiMenuHandler;
import bot.menu.SimpleMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.cards.CardsGlobalManager;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.time.Instant;
import java.util.List;

public class DropCommand extends AbstractCommandNoArguments implements EmojiMenuHandler {

    public DropCommand(AnimeCardsGame game) {
        super(game, CommandPermissions.USER);
        name = "drop";
    }

    @Override
    public void handle(CommandEvent event) {

        CooldownSet cooldowns = game.getCooldowns(player.getId());
        Instant now = Instant.now();
        if (cooldowns.checkDrop(now)){
            sendMessage(event, "your drop cooldown is " + cooldowns.getDropTimeLeft(now));
        }

        cooldowns.useDrop(now);

        CardsGlobalManager collection = game.getCardsGlobalManager();
        List<CardGlobal> cards = collection.getRandomCards(3);

        SimpleMenuCreator.menuDropCards(cards, game, event, this);
    }

    @Override
    public void hEmojiOne(MessageReactionAddEvent event, AnimeCardsGame game) {
        chooseCardForGrab(event, game, 0);
    }

    @Override
    public void hEmojiTwo(MessageReactionAddEvent event, AnimeCardsGame game) {
        chooseCardForGrab(event, game, 1);
    }

    @Override
    public void hEmojiThree(MessageReactionAddEvent event, AnimeCardsGame game) {
        chooseCardForGrab(event, game, 2);
    }

    private void chooseCardForGrab(MessageReactionAddEvent event, AnimeCardsGame game, int cardIndex) {
        List<CardGlobal> cards = game.getDropManager().get(event.getMessageId());

        if (cards == null){
            event.getChannel().sendMessage("cannot grab any more").queue();
            return;
        }
        CardGlobal cardGlobal = cards.get(cardIndex);
        grabCard(event, cardGlobal);
    }

    private void grabCard(MessageReactionAddEvent event, CardGlobal cardGlobal) {
        Player player = game.getPlayerById(event.getUserId());

        CooldownSet cooldowns = game.getCooldowns(player.getId());
        Instant now = Instant.now();
        if (cooldowns.checkDrop(now)){
            sendMessage(event, "your grab cooldown is " + cooldowns.getDropTimeLeft(now));
        }
        
        cooldowns.useGrab(now);

        CardPersonal cardPersonal = game.pickPersonalCardDelay(player, cardGlobal.getId(), 0);
        if (cardPersonal != null){
            sendMessage(event, "you've picked card " + cardPersonal.getIdName());
        }else{
            sendMessage(event, "cannot find global card " + cardGlobal.getId());
        }
    }
}