package bot.commands.user.squadron;

import bot.commands.user.shop.MessageSenderTester;
import game.squadron.PatrolType;
import game.squadron.Squadron;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class PatrolCommandTest extends MessageSenderTester {

    private Squadron spySquadron;

    @BeforeEach
    void setUp() {
        spySquadron = spy(sender.getGame().getOrCreateSquadron(tester()));
        tester().setSquadron(spySquadron);
    }

    @Test
    void testPatrolNotStartedForEmptySquadron() {
        spySquadron.setMembers(new ArrayList<>());
        send("#patrol overworld");
        verify(spySquadron, never()).startPatrol(any(PatrolType.class), any(Instant.class));
    }

    @Test
    void testPatrolStartedFullName() {
        spySquadron.addMember(tester().getCards().get(0));
        send("#patrol overworld");

        sender.assertCommandHandled(PatrolCommand.class);
        verify(spySquadron, times(1)).startPatrol(eq(PatrolType.OVERWORLD), any(Instant.class));
    }

    @Test
    void testPatrolWorldAlias() {
        spySquadron.addMember(tester().getCards().get(0));
        send("#patrol o");

        sender.assertCommandHandled(PatrolCommand.class);
        verify(spySquadron, times(1)).startPatrol(eq(PatrolType.OVERWORLD), any(Instant.class));
    }

    @Test
    void testPatrolNotStarted_WhenAnotherPatrolActive() {

    }
}