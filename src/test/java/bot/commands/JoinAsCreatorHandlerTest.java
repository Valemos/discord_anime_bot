package bot.commands;

import bot.commands.handlers.creator.JoinAsCreatorHandler;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import bot.AccessLevel;
import game.Player;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class JoinAsCreatorHandlerTest {
    Player player;
    MessageChannel mChannel;

    @BeforeEach
    void setUp() {
        player = new Player("1", AccessLevel.USER);
        mChannel = mock(MessageChannel.class);
        doReturn(mock(MessageAction.class)).when(mChannel).sendMessage(anyString());
    }

    @Test
    void testFirstUserIsNotCreator() {
        assertNotSame(AccessLevel.CREATOR, player.getAccessLevel());
    }

    @Test
    void testUserBecameCreator() {
        JoinAsCreatorHandler handler = new JoinAsCreatorHandler();
        CommandParameters params = new CommandParameters(null, player, null, mChannel, null);
        handler.handle(params);

        assertSame(player.getAccessLevel(), AccessLevel.CREATOR);
    }
}
