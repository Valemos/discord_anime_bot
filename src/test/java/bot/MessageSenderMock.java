package bot;

import bot.commands.CommandParameters;
import bot.commands.handlers.BotCommandHandler;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CharacterCardGlobal;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MessageSenderMock {


    public Player player;
    public Player admin;
    public Player creator;
    private Player defaultPlayer;

    public MessageCommandsHandler handler;
    private final MessageCommandsHandler spyHandler;
    private final HashMap<Class<? extends BotCommandHandler>, BotCommandHandler> spyCommands;
    private final AnimeCardsGame game;
    public AnimeCardsGame spyGame;

    public ArgumentCaptor<CharacterCardGlobal> cardArgument = ArgumentCaptor.forClass(CharacterCardGlobal.class);
    MessageChannel mChannel;
    MessageReceivedEvent mMessageEvent;
    Message mMessage;
    User mUser;


    public MessageSenderMock(AnimeCardsGame game, List<BotCommandHandler> commands) {
        player = new Player("1", AccessLevel.USER);
        admin = new Player("2", AccessLevel.ADMIN);
        creator = new Player("3", AccessLevel.CREATOR);

        defaultPlayer = player;

        this.game = game;
        game.addPlayer(player);
        game.addPlayer(admin);
        game.addPlayer(creator);

        spyCommands = new HashMap<>();
        for(BotCommandHandler command : commands){
            spyCommands.put(command.getClass(), spy(command));
        }

        spyGame = Mockito.spy(game);
        handler = new MessageCommandsHandler(spyGame);
        handler.setCommands(spyCommands.values());
        spyHandler = spy(handler);

        mChannel = mock(MessageChannel.class);
        doReturn(mock(MessageAction.class)).when(mChannel).sendMessage(anyString());

        mMessage = mock(Message.class);
        mUser = mock(User.class);

        mMessageEvent = mock(MessageReceivedEvent.class);
        when(mMessageEvent.getChannel()).thenReturn(mChannel);
        when(mMessageEvent.getMessage()).thenReturn(mMessage);
        when(mMessageEvent.getAuthor()).thenReturn(mUser);

    }

    public void setDefaultPlayer(Player player){
        defaultPlayer = player;
    }

    public void sendMessage(String messageString) {
        sendMessage(messageString, defaultPlayer.getId());
    }

    public void sendMessage(String messageString, String userId) {
        when(mMessage.getContentRaw()).thenReturn(messageString);
        when(mUser.getId()).thenReturn(userId);

        spyHandler.onMessageReceived(mMessageEvent);
    }

    public void assertCommandNotHandled(Class<? extends BotCommandHandler> commandClass) {
        BotCommandHandler currentSpyCommand = spyCommands.get(commandClass);
        verify(currentSpyCommand, never()).handle(any());
    }

    public void assertCommandNotHandledOnMessage(String message, Class<? extends BotCommandHandler> commandClass){
        sendMessage(message);
        assertCommandNotHandled(commandClass);
    }

    public void assertNoCommandHandled() {
        verify(spyHandler, never()).handleCommandMessageForPlayer(any(), any(), any(), any(), any());
    }

    public void assertAnyCommandHandled() {
        verify(spyHandler).handleCommandMessageForPlayer(any(), any(), any(), any(), any());
    }

    public BotCommandHandler getCommandSpy(Class<? extends BotCommandHandler> handlerClass) {
        return spyCommands.get(handlerClass);
    }

    public void assertCommandHandled(Class<? extends BotCommandHandler> commandClass) {
        BotCommandHandler currentSpyCommand = spyCommands.get(commandClass);
        verify(currentSpyCommand).handle(any());
    }

    public void assertNoCommandHandledOnMessage(String messageString) {
        assertNoCommandHandledOnMessage(messageString, defaultPlayer.getId());
    }

    public void assertNoCommandHandledOnMessage(String messageString, String userId) {
        sendMessage(messageString, userId);
        assertNoCommandHandled();
    }

    public void assertCommandHandledOnMessage(String messageString, Class<? extends BotCommandHandler> handlerClass) {
        assertCommandHandledOnMessage(messageString, defaultPlayer.getId(), handlerClass);
    }

    public void assertCommandHandledOnMessage(String messageString,
                                              String userId,
                                              Class<? extends BotCommandHandler> commandClass) {
        sendMessage(messageString, userId);
        assertCommandHandled(commandClass);
    }

    public CommandParameters captureLastCommandParameters(Class<? extends BotCommandHandler> handlerClass) {
        BotCommandHandler handler = spyCommands.get(handlerClass);
        ArgumentCaptor<CommandParameters> argumentCaptor = ArgumentCaptor.forClass(CommandParameters.class);
        verify(handler).handle(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

    public void assertAnyCommandHandledOnMessage(String s) {
        sendMessage(s);
        assertAnyCommandHandled();
    }
}
