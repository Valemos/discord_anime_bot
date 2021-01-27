package bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class MessageEventMock {

    @Mock
    private MessageReceivedEvent mMessageEvent;
    @Mock
    private Message mMessage;
    @Mock
    private RestAction<Void> mVoidRestAction;
    @Mock
    private User mUser;
    @Mock
    private TextChannel mTextChannel;
    @Mock
    private MessageChannel mMessageChannel;
    @Mock
    public MessageAction mMessageAction;
    @Mock
    private JDA mJDA;

    public MessageEventMock() {
        MockitoAnnotations.initMocks(this);
    }

    public void initMessageEventMock() {
        when(mMessageEvent.getMessage()).thenReturn(mMessage);
        when(mMessageEvent.getAuthor()).thenReturn(mUser);
        setEventChannel(mMessageEvent, ChannelType.TEXT);
        setMessageChannelMock();
    }

    public MessageReceivedEvent getMock() {
        return mMessageEvent;
    }

    private void setMessageMock(String messageId) {
        setMessageMock(messageId, "");
    }

    private void setMessageMock(String messageId, String message) {
        if (messageId != null) when(mMessage.getId()).thenReturn(messageId);
        when(mMessage.getContentRaw()).thenReturn(message);
        when(mMessage.editMessage(any(Message.class))).thenReturn(mMessageAction);
        when(mMessage.addReaction(anyString())).thenReturn(mVoidRestAction);
        when(mMessage.getChannel()).thenReturn(mMessageChannel);
    }

    protected void setEventMessage(MessageReactionAddEvent event, String messageId, String playerId) {
        when(event.getMessageId()).thenReturn(messageId);
        when(event.getUserId()).thenReturn(playerId);
    }

    private void setUserMock(String userId) {
        when(mUser.getId()).thenReturn(userId);
        when(mUser.isBot()).thenReturn(false);
    }

    private void setMessageChannelMock() {
        when(mMessageChannel.getJDA()).thenReturn(mJDA);
        when(mMessageChannel.sendMessage(anyString())).thenReturn(mMessageAction);
        when(mMessageChannel.sendMessage(any(MessageEmbed.class))).thenReturn(mMessageAction);
        when(mMessageChannel.sendMessage(any(Message.class))).thenReturn(mMessageAction);
    }

    protected void setEventChannel(GenericMessageEvent event, ChannelType channelType) {
        when(event.getJDA()).thenReturn(mJDA);
        when(event.getChannel()).thenReturn(mMessageChannel);
        when(event.getChannelType()).thenReturn(channelType);
        when(event.getTextChannel()).thenReturn(mTextChannel);
        when(mTextChannel.canTalk()).thenReturn(true);
    }

    public void setUser(String userId) {
        setUserMock(userId);
    }

    public void setMessage(String messageId) {
        setMessage(messageId, null);
    }

    public void setMessage(String messageId, String message) {
        setMessageMock(messageId, message);
    }

    public MessageAction getAction() {
        return mMessageAction;
    }

    public Message getMessage() {
        return mMessage;
    }

    public User getUser() {
        return mUser;
    }

    public JDA getJDA() {
        return mJDA;
    }
}
