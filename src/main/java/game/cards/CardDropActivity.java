package game.cards;

import bot.commands.user.carddrop.CardDropMenu;
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

public class CardDropActivity {

    private final CardDropMenu menu;
    List<CardGlobal> cards;

    private Instant timeStarted;
    private final Map<String, CardPersonal> fightingPlayerCards = new HashMap<>();

    public CardDropActivity(List<CardGlobal> cards) {
        this.cards = cards;
        menu = new CardDropMenu(this);
    }

    public void setCards(List<CardGlobal> cards) {
        this.cards = cards;
    }

    public void setTimeStarted(Instant timeStarted) {
        this.timeStarted = timeStarted;
    }

    public List<CardGlobal> getCards() {
        return cards;
    }

    public CardDropMenu getMenu() {
        return menu;
    }

    public void finishFight() {
        for (String playerId : fightingPlayerCards.keySet()){
            CardPersonal card = fightingPlayerCards.get(playerId);
            if(decidePlayerGetsCard(playerId, card)){
                menu.notifyCardReceived(playerId, card);
            }
        }

        menu.close();
    }

    private boolean decidePlayerGetsCard(String playerId, CardPersonal card) {
        return card != null;
    }

    public void selectCard(MessageReactionAddEvent event, AnimeCardsGame game, int cardIndex) {
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

    public void createMenu(MessageChannel channel, AnimeCardsGame game) {
        menu.createMenu(channel, game);
    }

}
