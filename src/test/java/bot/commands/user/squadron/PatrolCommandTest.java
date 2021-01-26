package bot.commands.user.squadron;

import bot.commands.user.shop.MessageSenderTester;
import game.cards.CardPersonal;
import game.player_objects.squadron.PatrolType;
import game.player_objects.squadron.Squadron;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class PatrolCommandTest extends MessageSenderTester {

    @BeforeEach
    void setUp() {
        sender.clearSquadronsTable();
    }

    private void addSquadronMember(int index) {
        CardPersonal card = getTesterCard(index);
        game().addSquadronMember(tester(), card);
        reset(game());
    }

    @Test
    void testPatrolNotStartedForEmptySquadron() {
        getTesterSquadron().setMembers(new HashSet<>());
        send("#patrol overworld");
        verify(game(), never()).createNewPatrol(any(), any(), any());
    }

    @Test
    void testPatrolStartedFullName() {
        addSquadronMember(0);
        send("#patrol overworld");

        sender.assertCommandHandled(PatrolCommand.class);
        verify(game(), times(1)).createNewPatrol(any(), eq(PatrolType.OVERWORLD), any());
    }

    @Test
    void testPatrolWorldAlias() {
        addSquadronMember(0);
        send("#patrol o");

        sender.assertCommandHandled(PatrolCommand.class);
        verify(game(), atLeastOnce()).createNewPatrol(any(), eq(PatrolType.OVERWORLD), any());
    }

    @Test
    void testPatrolNotStarted_WhenAnotherPatrolActive() {
        addSquadronMember(0);

        send("#patrol o");
        verify(game(), times(1)).createNewPatrol(any(), any(), any());

        assertTrue(tester().getSquadron().getPatrol().isStarted());
        send("#patrol o");
        verify(game(), times(1)).createNewPatrol(any(), any(), any());
    }

    @Test
    void testCannotStopPatrol_whenNotActive() {
        assertFalse(getTesterSquadron().getPatrol().isStarted());
        send("#patrolstop");
        verify(game(), never()).finishPatrol(any(), any());
    }

    @Test
    void testPatrolStopped_whenActive() {
        addSquadronMember(0);
        send("#patrol o");
        assertTrue(tester().getSquadron().getPatrol().isStarted());

        send("#patrolstop");
        verify(game()).finishPatrol(any(), any());
    }

    @Test
    void testAddNotExistingMembersToSquadron() {
        assertTrue(getTesterSquadron().isEmpty());
        send("#squadronadd 12345678 asdfghjkl");
        assertTrue(getTesterSquadron().isEmpty());
    }

    @Test
    void testRemoveNotExistingFromSquadron() {
        addSquadronMember(0);
        assertEquals(1, tester().getSquadron().getMembers().size());
        send("#squadronremove 12345678 asdfghjkl");
        assertEquals(1, tester().getSquadron().getMembers().size());
    }

    @Test
    void testCannotAddToSquadron_WhenInPatrol() {
        addSquadronMember(0);
        send("#patrol o");

        send("#squadronadd 12345678 " + getTesterCard(0).getId());

        verify(game(), never()).addSquadronMember(any(), any());
    }

    @Test
    void testCannotRemoveFromSquadron_WhenInPatrol() {
        addSquadronMember(0);
        send("#patrol o");

        send("#squadronremove 12345678 " + getTesterCard(0).getId());

        verify(game(), never()).removeSquadronMembers(any(), any());
    }

    @Test
    void testAddMembersToSquadron() {
        assertFalse(getTesterSquadron().getMembers().contains(getTesterCard(0)));
        assertFalse(getTesterSquadron().getMembers().contains(getTesterCard(1)));

        send("#squadronadd " + getTesterCard(0).getId() + " " + getTesterCard(1).getId());

        assertTrue(getTesterSquadron().getMembers().contains(getTesterCard(0)));
        assertTrue(getTesterSquadron().getMembers().contains(getTesterCard(1)));
    }

    @NotNull
    private Squadron getTesterSquadron() {
        return game().getOrCreateSquadron(tester());
    }

    @Test
    void testRemoveMembersFromSquadron() {
        addSquadronMember(0);
        addSquadronMember(1);
        addSquadronMember(3);

        assertTrue(getTesterSquadron().getMembers().contains(getTesterCard(0)));
        assertTrue(getTesterSquadron().getMembers().contains(getTesterCard(1)));

        send("#squadronremove " + getTesterCard(0).getId() + " " + getTesterCard(1).getId());

        assertFalse(getTesterSquadron().getMembers().contains(getTesterCard(0)));
        assertFalse(getTesterSquadron().getMembers().contains(getTesterCard(1)));
    }
}