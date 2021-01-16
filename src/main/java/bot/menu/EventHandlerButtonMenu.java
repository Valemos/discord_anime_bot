package bot.menu;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Menu;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;

public class EventHandlerButtonMenu extends Menu
{
    private final Color color;
    private final String text;
    private String description;
    private final List<String> choices;
    private final Consumer<MessageReactionAddEvent> action;
    private final Consumer<Message> finalAction;

    EventHandlerButtonMenu(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit,
                           Color color, String text, String description, List<String> choices, Consumer<MessageReactionAddEvent> action, Consumer<Message> finalAction)
    {
        super(waiter, users, roles, timeout, unit);
        this.color = color;
        this.text = text;
        this.description = description;
        this.choices = choices;
        this.action = action;
        this.finalAction = finalAction;
    }

    @Override
    public void display(MessageChannel channel)
    {
        initialize(channel.sendMessage(getMessage()));
    }

    @Override
    public void display(Message message)
    {
        initialize(message.editMessage(getMessage()));
    }

    // Initializes the ButtonMenu using a Message RestAction
    // This is either through editing a previously existing Message
    // OR through sending a new one to a TextChannel.
    private void initialize(RestAction<Message> ra)
    {
        ra.queue(m -> {
            for(int i=0; i<choices.size(); i++)
            {
                // Get the emote to display.
                Emote emote;
                try {
                    emote = m.getJDA().getEmoteById(choices.get(i));
                } catch(Exception e) {
                    emote = null;
                }
                // If the emote is null that means that it might be an emoji.
                // If it's neither, that's on the developer and we'll let it
                // throw an error when we queue a rest action.
                RestAction<Void> r = emote==null ? m.addReaction(choices.get(i)) : m.addReaction(emote);
                if(i+1<choices.size()) {
                    r.queue();
                }else{
                    r.queue(v -> selectionMenuWaitReactions(m));
                }
            }
        });
    }

    private void selectionMenuWaitReactions(Message message) {

        waiter.waitForEvent(MessageReactionAddEvent.class,
                event -> {
                    if(!event.getMessageId().equals(message.getId())) return false;

                    String reaction = getReaction(event);
                    if (!choices.contains(reaction)) return false;

                    return isValidUser(event.getUser(), event.isFromGuild() ? event.getGuild() : null);
                },
                event -> handleAcceptReaction(event, message),
                timeout, unit, () -> finalAction.accept(message));
    }

    @NotNull
    private String getReaction(MessageReactionAddEvent event) {
        return event.getReaction().getReactionEmote().isEmote()
                ? event.getReaction().getReactionEmote().getId()
                : event.getReaction().getReactionEmote().getName();
    }

    private void handleAcceptReaction(MessageReactionAddEvent event, Message message) {

        action.accept(event);

        try {
            User user = event.getUser();
            if (user != null){
                event.getReaction().removeReaction(user).queue();
            }
        } catch(PermissionException ignored) {}

        message.editMessage(getMessage()).queue(this::selectionMenuWaitReactions);
    }

    // Generates a ButtonMenu message
    public Message getMessage()
    {
        MessageBuilder mbuilder = new MessageBuilder();
        if(text!=null)
            mbuilder.append(text);
        if(description!=null)
            mbuilder.setEmbed(new EmbedBuilder().setColor(color).setDescription(description).build());
        return mbuilder.build();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder extends Menu.Builder<EventHandlerButtonMenu.Builder, EventHandlerButtonMenu>
    {
        private Color color;
        private String text;
        private String description;
        private final List<String> choices = new LinkedList<>();
        private Consumer<MessageReactionAddEvent> action;
        private Consumer<Message> finalAction = m -> m.delete().queue();

        @Override
        public EventHandlerButtonMenu build()
        {
            Checks.check(waiter != null, "Must set an EventWaiter");
            Checks.check(!choices.isEmpty(), "Must have at least one choice");
            Checks.check(action != null, "Must provide an action consumer");
            Checks.check(text != null || description != null, "Either text or description must be set");

            return new EventHandlerButtonMenu(waiter, users, roles, timeout, unit, color, text, description, choices, action, finalAction);
        }

        public EventHandlerButtonMenu.Builder setColor(Color color)
        {
            this.color = color;
            return this;
        }

        public EventHandlerButtonMenu.Builder setText(String text)
        {
            this.text = text;
            return this;
        }

        public EventHandlerButtonMenu.Builder setDescription(String description)
        {
            this.description = description;
            return this;
        }

        public EventHandlerButtonMenu.Builder setAction(Consumer<MessageReactionAddEvent> action)
        {
            this.action = action;
            return this;
        }

        public EventHandlerButtonMenu.Builder setFinalAction(Consumer<Message> finalAction)
        {
            this.finalAction = finalAction;
            return this;
        }

        public EventHandlerButtonMenu.Builder addChoice(String emoji)
        {
            this.choices.add(emoji);
            return this;
        }

        public EventHandlerButtonMenu.Builder addChoice(Emote emote)
        {
            return addChoice(emote.getId());
        }

        public EventHandlerButtonMenu.Builder addChoices(String... emojis)
        {
            for(String emoji : emojis)
                addChoice(emoji);
            return this;
        }

        public EventHandlerButtonMenu.Builder addChoices(Emote... emotes)
        {
            for(Emote emote : emotes)
                addChoice(emote);
            return this;
        }

        public EventHandlerButtonMenu.Builder setChoices(String... emojis)
        {
            this.choices.clear();
            return addChoices(emojis);
        }

        public EventHandlerButtonMenu.Builder setChoices(MenuEmoji... emojis)
        {
            this.choices.clear();
            for (MenuEmoji emoji : emojis){
                addChoice(emoji.getEmoji());
            }
            return this;
        }

        public EventHandlerButtonMenu.Builder setChoices(Emote... emotes)
        {
            this.choices.clear();
            return addChoices(emotes);
        }
    }
}
