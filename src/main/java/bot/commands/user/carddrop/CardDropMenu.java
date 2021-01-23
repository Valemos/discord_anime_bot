package bot.commands.user.carddrop;

import bot.menu.EmojiMenuHandler;
import bot.menu.EventHandlerButtonMenu;
import bot.menu.MenuEmoji;
import game.AnimeCardsGame;
import game.cards.CardDropActivity;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.time.Instant;
import java.util.List;

public class CardDropMenu implements EmojiMenuHandler {
    private EventHandlerButtonMenu menu;
    private Message menuMessage;

    private final CardDropActivity cardDrop;

    public CardDropMenu(CardDropActivity cardDrop) {
        this.cardDrop = cardDrop;
    }

    public boolean exists() {
        return menuMessage == null;
    }

    @Override
    public void hEmojiOne(MessageReactionAddEvent event, AnimeCardsGame game) {
        cardDrop.selectCard(event, game, 0);
    }

    @Override
    public void hEmojiTwo(MessageReactionAddEvent event, AnimeCardsGame game) {
        cardDrop.selectCard(event, game, 1);
    }

    @Override
    public void hEmojiThree(MessageReactionAddEvent event, AnimeCardsGame game) {
        cardDrop.selectCard(event, game, 2);
    }

    public void notifyCardReceived(String playerId, CardPersonal card) {
        MessageChannel channel = menuMessage.getChannel();
        channel.getJDA().retrieveUserById(playerId).queue(
                user -> {
                    channel.sendMessage(user.getName() + " received card " + card.getIdName()).queue();
                }
        );
    }

    public void close() {
        menu.stopWaitingReactions();
        menu.setAdditionalInfo("cannot grab cards any more");
        menu.display(menuMessage);
    }

    public void createMenu(MessageChannel channel, AnimeCardsGame game) {
        long fightSeconds = game.getDropManager().getFightSeconds();
        menu = new EventHandlerButtonMenu.Builder()
                    .setEventWaiter(game.getEventWaiter())
                    .setText("Drop cards")
                    .setDescription(getCardsDescription(cardDrop.getCards()))
                    .setAdditionalInfo("you have " + fightSeconds + " seconds to grab!")
                    .setChoices(MenuEmoji.ONE, MenuEmoji.TWO, MenuEmoji.THREE)
                    .setAction(e -> hReactionAddEvent(e, game))
                    .resetReactionEmotes(false)
                    .build();

        menuMessage = menu.getMessage();
        channel.sendMessage(menuMessage).queue(
                msg -> {
                    menuMessage = msg;
                    menu.display(msg);
                    cardDrop.setTimeStarted(Instant.now());
                    game.getDropManager().startActivity(msg.getId(), cardDrop);
                }
        );
    }

    private static String getCardsDescription(List<CardGlobal> cards) {
        StringBuilder description = new StringBuilder();
        int counter = 1;
        for (CardGlobal card : cards){
            description.append(counter++).append(". ").append(card.getNameStats()).append('\n');
        }
        return description.toString();
    }
}
