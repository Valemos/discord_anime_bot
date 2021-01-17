package bot.menu;

import game.AnimeCardsGame;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface EmojiMenuHandler {

    default void hReactionAddEvent(MessageReactionAddEvent event, AnimeCardsGame game) {
        String emoji = event.getReactionEmote().getEmoji();

        if (MenuEmoji.CONFIRM.getEmoji().equals(emoji)) {
            hEmojiConfirm(event, game);

        } else if (MenuEmoji.DISCARD.getEmoji().equals(emoji)){
            hEmojiDiscard(event, game);

        } else if (MenuEmoji.SHOW_MORE.getEmoji().equals(emoji)){
            hEmojiShowMore(event, game);

        } else if (MenuEmoji.ONE.getEmoji().equals(emoji)){
            hEmojiOne(event, game);

        } else if (MenuEmoji.TWO.getEmoji().equals(emoji)){
            hEmojiTwo(event, game);

        } else if (MenuEmoji.THREE.getEmoji().equals(emoji)){
            hEmojiThree(event, game);

        }
    }

    default void hEmojiOne(MessageReactionAddEvent event, AnimeCardsGame game) {}

    default void hEmojiTwo(MessageReactionAddEvent event, AnimeCardsGame game) {}

    default void hEmojiThree(MessageReactionAddEvent event, AnimeCardsGame game) {}

    default void hEmojiDiscard(AnimeCardsGame game, MessageChannel channel, String messageId) {}

    default void hEmojiDiscard(MessageReactionAddEvent event, AnimeCardsGame game) {
        hEmojiDiscard(game, event.getChannel(), event.getMessageId());
    }

    default void hEmojiConfirm(MessageReactionAddEvent event, AnimeCardsGame game) {}

    default void hEmojiShowMore(MessageReactionAddEvent event, AnimeCardsGame game) {}
}
