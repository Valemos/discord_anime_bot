package bot.commands.user;


import bot.CommandPermissions;
import bot.commands.AbstractCommandNoArguments;
import bot.menu.MenuEmoji;
import bot.menu.SimpleMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.cards.CardsGlobalManager;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.List;

public class DropCommand extends AbstractCommandNoArguments {

    public DropCommand(AnimeCardsGame game) {
        super(game, CommandPermissions.USER);
        name = "drop";
    }

    @Override
    public void handle(CommandEvent event) {
        CardsGlobalManager collection = game.getCardsGlobalManager();
        List<CardGlobal> cards = collection.getRandomCards(3);

        SimpleMenuCreator.showDropCards(cards, game, event, this::handleCardChosen);
    }

    private void handleCardChosen(MessageReactionAddEvent event) {
        String emoji = event.getReactionEmote().getEmoji();
        String messageId = event.getMessageId();

        List<CardGlobal> cards = game.getDropManager().get(messageId);
        if (cards == null){
            event.getChannel().sendMessage("cannot grab any more").queue();
            return;
        }

        CardGlobal cardGlobal;
        if (    MenuEmoji.ONE.getEmoji().equals(emoji))     cardGlobal = cards.get(0);
        else if (MenuEmoji.TWO.getEmoji().equals(emoji))    cardGlobal = cards.get(1);
        else if (MenuEmoji.THREE.getEmoji().equals(emoji))  cardGlobal = cards.get(2);
        else return;

        Player player = game.getPlayerById(event.getUserId());
        CardPersonal cardPersonal = game.pickPersonalCardDelay(player, cardGlobal.getId(), 0);

        if (cardPersonal != null){
            event.getChannel().sendMessage("you've picked card " + cardPersonal.getIdName()).queue();
        }else{
            event.getChannel().sendMessage("cannot find global card " + cardGlobal.getId()).queue();
        }
    }
}