package game.cards;

import bot.menu.EmojiMenuHandler;
import bot.menu.EventHandlerButtonMenu;
import bot.menu.MenuEmoji;
import game.AnimeCardsGame;
import game.Player;
import game.cooldown.CooldownSet;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDropActivity implements EmojiMenuHandler {
    private EventHandlerButtonMenu menu;
    List<CardGlobal> cards;

    private Instant timeStarted;
    private final Map<String, CardPersonal> fightingPlayerCards = new HashMap<>();
    private Message menuMessage;

    public CardDropActivity(List<CardGlobal> cards) {
        this.cards = cards;
    }


    public void setCards(List<CardGlobal> cards) {
        this.cards = cards;
    }

    public List<CardGlobal> getCards() {
        return cards;
    }

    public void finishFight() {
        if (menuMessage == null) {
            return;
        }

        for (String playerId : fightingPlayerCards.keySet()){
            CardPersonal card = fightingPlayerCards.get(playerId);
            if(decidePlayerGetsCard(playerId, card)){
                sendCardReceivedMessage(menuMessage.getChannel(), playerId, card);
            }
        }

        menu.setAdditionalInfo("cannot grab cards any more");
        menu.display(menuMessage);
        menu.stopWaitingReactions();
    }

    private boolean decidePlayerGetsCard(String playerId, CardPersonal card) {
        return true;
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
        Instant now = Instant.now();

        Player player = game.getPlayerById(event.getUserId());

        CooldownSet cooldowns = game.getCooldowns(player.getId());
        if (cooldowns.getGrab().tryUse(now)){
            fightForCard(now, game, player, cardIndex);
        }else{
            event.getChannel().sendMessage(cooldowns.getGrab().getVerboseDescription(now)).queue();
        }
    }

    private void fightForCard(Instant timePicked, AnimeCardsGame game, Player player, int cardIndex) {
        float cardPickDelay = (float) Duration.between(timeStarted, timePicked).toMillis() / 1000;

        CardPersonal card = game.pickPersonalCard(player, cards.get(cardIndex), cardPickDelay);
        fightingPlayerCards.put(player.getId(), card);
    }

    private void sendCardReceivedMessage(MessageChannel channel, String playerId, CardPersonal card) {
        channel.getJDA().retrieveUserById(playerId).queue(
                user -> {
                    channel.sendMessage(user.getName() + " received card " + card.getIdName()).queue();
                }
        );
    }

    public EventHandlerButtonMenu createMenu(AnimeCardsGame game) {
        return new EventHandlerButtonMenu.Builder()
                .setEventWaiter(game.getEventWaiter())
                .setText("Drop cards")
                .setDescription(getCardsDescription(cards))
                .setAdditionalInfo("fight not started")
                .setChoices(MenuEmoji.ONE, MenuEmoji.TWO, MenuEmoji.THREE)
                .setAction(e -> hReactionAddEvent(e, game))
                .resetReactionEmotes(false)
                .build();
    }

    public void showMenu(MessageChannel channel, AnimeCardsGame game) {
        menu = createMenu(game);

        menuMessage = menu.getMessage();
        channel.sendMessage(menuMessage).queue(
                msg -> {
                    menuMessage = msg;
                    menu.display(msg);
                    timeStarted = Instant.now();
                    game.getDropManager().add(msg.getId(), this);
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

    public void setSecondsLeft(int secondsLeft) {
        menu.setAdditionalInfo(secondsLeft + " seconds left");
        menu.display(menuMessage);
    }
}
