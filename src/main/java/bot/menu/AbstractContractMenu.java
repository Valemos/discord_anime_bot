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
                    game.getContractsManager().add(resultMessage.getId(), contractClass, contract);
                }
        );
    }

    @Override
    public void hEmojiConfirm(MessageReactionAddEvent event, AnimeCardsGame game) {

        ContractInterface contract = game.getContractsManager().get(contractClass, event.getMessageId());
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
        ContractInterface contract = game.getContractsManager().get(contractClass, messageId);
        contract.discard();
        game.getContractsManager().removeContract(contractClass, messageId);
    }

    @Override
    public void hEmojiShowMore(MessageReactionAddEvent event, AnimeCardsGame game) {
        ContractInterface contract = game.getContractsManager().get(contractClass, event.getMessageId());
        if (contract != null){
            event.getChannel().sendMessage(contract.getMoreInfo()).queue();
        }
    }
}
