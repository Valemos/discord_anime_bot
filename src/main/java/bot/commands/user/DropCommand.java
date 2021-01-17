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

import java.util.List;

public class DropCommand extends AbstractCommandNoArguments implements EmojiMenuHandler {

    public DropCommand(AnimeCardsGame game) {
        super(game, CommandPermissions.USER);
        name = "drop";
    }

    @Override
    public void handle(CommandEvent event) {
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
        Player player = this.game.getPlayerById(event.getUserId());
        CardPersonal cardPersonal = this.game.pickPersonalCardDelay(player, cardGlobal.getId(), 0);

        if (cardPersonal != null){
            event.getChannel().sendMessage("you've picked card " + cardPersonal.getIdName()).queue();
        }else{
            event.getChannel().sendMessage("cannot find global card " + cardGlobal.getId()).queue();
        }
    }
}