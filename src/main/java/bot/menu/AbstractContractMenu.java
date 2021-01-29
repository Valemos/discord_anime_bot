package bot.menu;

import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.contract.AbstractContract;
import game.contract.ContractInterface;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public abstract class AbstractContractMenu<T extends AbstractContract> implements EmojiMenuHandler {
    private final Class<? extends AbstractContract> contractClass;
    protected final T contract;
    AnimeCardsGame game;
    private final String menuTitle;

    public AbstractContractMenu(AnimeCardsGame game, T contract, String menuTitle) {
        this.menuTitle = menuTitle;
        this.game = game;

        this.contractClass = contract.getClass();
        this.contract = contract;
        game.getContractsManager().add(contract);
    }

    public void sendMenu(CommandEvent event) {
        EventHandlerButtonMenu menu = buildMenu(event, menuTitle, contract.getMenuDescription());
        displayMenu(event.getChannel(), menu);
    }

    protected EventHandlerButtonMenu buildMenu(CommandEvent event, String title, String description) {
        return new EventHandlerButtonMenu.Builder()
                .setEventWaiter(game.getEventWaiter())
                .setUsers(event.getAuthor())
                .setText(title)
                .setChoices(MenuEmoji.CONFIRM, MenuEmoji.DISCARD, MenuEmoji.SHOW_MORE)
                .setAction(e -> hReactionAddEvent(e, game))
                .setFinalAction(m -> hEmojiDiscard(game, event.getChannel(), m.getId()))
                .setDescription(description)
                .build();
    }

    protected void displayMenu(MessageChannel channel, EventHandlerButtonMenu menu) {
        Message message = new MessageBuilder().append("...").build();
        channel.sendMessage(message).queue(
                resultMessage -> {
                    menu.display(resultMessage);
                    game.getContractsManager().addMessage(resultMessage.getId(), contract);
                }
        );
    }

    @Override
    public void hEmojiConfirm(MessageReactionAddEvent event, AnimeCardsGame game) {

        AbstractContract contract = game.getContractsManager().getForMessage(contractClass, event.getMessageId());
        if (contract == null) {
            event.getChannel().sendMessage("contract invalid").queue();
            return;
        }

        contract.confirm(game, event.getUserId());

        if (contract.isFinished()){
            event.getChannel().sendMessage("contract finished").queue();
        }
    }

    @Override
    public void hEmojiDiscard(AnimeCardsGame game, MessageChannel channel, String messageId) {
        AbstractContract contract = game.getContractsManager().getForMessage(contractClass, messageId);
        contract.discard();
        game.getContractsManager().removeMessageContract(messageId);
    }

    @Override
    public void hEmojiShowMore(MessageReactionAddEvent event, AnimeCardsGame game) {
        ContractInterface contract = game.getContractsManager().getForMessage(contractClass, event.getMessageId());
        if (contract != null){
            event.getChannel().sendMessage(contract.getMoreInfo()).queue();
        }
    }
}
