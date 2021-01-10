package bot.commands;

import bot.commands.handlers.creator.JoinAsCreatorCommand;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import bot.PlayerAccessLevel;
import game.Player;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class JoinAsCreatorCommandTest {
    Player player;
    MessageChannel mChannel;

    @BeforeEach
    void setUp() {
        player = new Player("1", PlayerAccessLevel.USER);
        mChannel = mock(MessageChannel.class);
        doReturn(mock(MessageAction.class)).when(mChannel).sendMessage(anyString());
    }

    @Test
    void testFirstUserIsNotCreator() {
        assertNotSame(PlayerAccessLevel.CREATOR, player.getAccessLevel());
    }

    @Test
    void testUserBecameCreator() {
        JoinAsCreatorCommand handler = new JoinAsCreatorCommand();
        CommandParameters params = new CommandParameters(null, player, null, mChannel, null);
        handler.handle(params);

        assertSame(player.getAccessLevel(), PlayerAccessLevel.CREATOR);
    }
}
