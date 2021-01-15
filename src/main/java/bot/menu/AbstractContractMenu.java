package bot.menu;

import bot.commands.MenuEmoji;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.contract.AbstractContract;
import game.contract.ContractInterface;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public abstract class AbstractContractMenu<T extends AbstractContract> {
    protected final T contract;
    private final Class<T> contractClass;
    AnimeCardsGame game;

    public AbstractContractMenu(AnimeCardsGame game, Class<T> contractClass, T contract) {
        this.game = game;
        this.contract = contract;
        this.contractClass = contractClass;
    }

    public abstract void sendMenu(CommandEvent event);


    protected EventHandlerButtonMenu buildMenu(CommandEvent event, String title, String description) {
        return new EventHandlerButtonMenu.Builder()
                .setEventWaiter(game.getEventWaiter())
                .setUsers(event.getAuthor())
                .setText(title)
                .setChoices(MenuEmoji.CONFIRM, MenuEmoji.DISCARD, MenuEmoji.SHOW_MORE)
                .setAction(this::handleEmojiChosen)
                .setFinalAction(m -> handleFinish(m.getId(), event.getChannel()))
                .setDescription(description)
                .build();
    }

    protected void displayMenu(MessageChannel channel, EventHandlerButtonMenu menu) {
        Message message = new MessageBuilder().append("d").build();
        channel.sendMessage(message).queue(
                resultMessage -> {
                    menu.display(resultMessage);
                    game.getContractsManager().add(resultMessage.getId(), contractClass, contract);
                }
        );
    }

    private void handleEmojiChosen(MessageReactionAddEvent event) {
        String emoji = event.getReactionEmote().getEmoji();
        String messageId = event.getMessageId();

        if (MenuEmoji.CONFIRM.getEmoji().equals(emoji)) {
            handleConfirm(messageId, game, event.getChannel());

        } else if (MenuEmoji.DISCARD.getEmoji().equals(emoji)){
            handleFinish(messageId, event.getChannel());

        } else if (MenuEmoji.SHOW_MORE.getEmoji().equals(emoji)){
            handleShowMoreInfo(event);
        }
    }

    private void handleConfirm(String messageId, AnimeCardsGame game, MessageChannel channel) {
        ContractInterface contract = game.getContractsManager().get(contractClass, messageId);
        if (contract != null){
            contract.complete(game);
        }
        handleFinish(messageId, channel);
    }

    private void handleFinish(String messageId, MessageChannel channel) {
        channel.sendMessage("contract finished").queue();
        game.getContractsManager().removeContract(contractClass, messageId);
    }

    private void handleShowMoreInfo(MessageReactionAddEvent event) {
        ContractInterface contract = game.getContractsManager().get(contractClass, event.getMessageId());
        if (contract != null){
            event.getChannel().sendMessage(contract.getMoreInfo()).queue();
        }
    }
}
