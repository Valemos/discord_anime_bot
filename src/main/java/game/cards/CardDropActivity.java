package game.cards;

import bot.commands.user.carddrop.CardDropMenu;
import game.AnimeCardsGame;
import game.Player;
import game.cooldown.CooldownSet;
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
    private final Map<Integer, CardFight> cardFights = new HashMap<>();

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

    public void finishFights(AnimeCardsGame game) {
        for (int cardGlobalIndex : cardFights.keySet()){

            CardFight cardFight = cardFights.get(cardGlobalIndex);

            String winnerId = cardFight.findWinner();
            CardPersonal cardPicked = cardFight.giveCardToWinner(game);

            menu.notifyCardReceived(winnerId, cardPicked);
        }

        menu.close();
    }

    public void selectCard(MessageReactionAddEvent event, AnimeCardsGame game, int cardIndex) {
        Instant now = Instant.now();

        Player player = game.getPlayer(event.getUserId());
        CooldownSet cooldowns = player.getCooldowns();

        if (cooldowns.getGrab().tryUse(now)){
            fightForCard(now, player, cardIndex);
        }else{
            event.getChannel().sendMessage(cooldowns.getGrab().getVerboseDescription(now)).queue();
        }
    }

    private void fightForCard(Instant timePicked, Player player, int cardIndex) {
        float cardPickDelay = (float) Duration.between(timeStarted, timePicked).toMillis() / 1000;

        CardFight currentFight = cardFights.getOrDefault(cardIndex, null);

        if (currentFight == null) {
            currentFight = new CardFight(cards.get(cardIndex));
            cardFights.put(cardIndex, currentFight);
        }

        currentFight.fight(player.getId(), cardPickDelay);
    }

    public void createMenu(MessageChannel channel, AnimeCardsGame game) {
        menu.createMenu(channel, game);
    }

}
