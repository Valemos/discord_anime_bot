package bot.commands.user.squadron;

import bot.commands.user.shop.MessageSenderTester;
import game.cards.CardPersonal;
import game.squadron.PatrolType;
import game.squadron.Squadron;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class PatrolCommandTest extends MessageSenderTester {

    private Squadron spySquadron;
    private Session spySession;

    @BeforeEach
    void setUp() {
        Squadron squadron = game().createNewSquadron(tester());

        spySquadron = spy(squadron);
        tester().setSquadron(spySquadron);

        spySession = spy(game().getDatabaseSession());
        game().setDatabaseSession(spySession);
        doAnswer(invocation -> {
            Squadron tempSquadron = new Squadron();
            tempSquadron.setId(spySquadron.getId());
            tempSquadron.setOwner(spySquadron.getOwner());
            tempSquadron.setPatrol(spySquadron.getPatrol());
            tempSquadron.setMembers(spySquadron.getMembers());
            tempSquadron.setPowerUps(spySquadron.getPowerUps());
            return spySession.merge(tempSquadron);
        }).when(spySession).merge(eq(spySquadron));

        doAnswer(inv -> {
            spySession.delete(spySession.merge(spySquadron));
            return null;
        }).when(spySession).delete(eq(spySquadron));
    }

    private void addSquadronMember(int index) {
        spySession.beginTransaction();
        CardPersonal card = getTesterCard(index);
        tester().getSquadron().addMember(card);
        spySession.merge(card);
        spySession.getTransaction().commit();
    }

    @Test
    void testPatrolNotStartedForEmptySquadron() {
        spySquadron.setMembers(new HashSet<>());
        send("#patrol overworld");
        verify(spySquadron, never()).startPatrol(any(), any());
    }

    @Test
    void testPatrolStartedFullName() {
        addSquadronMember(0);
        send("#patrol overworld");

        sender.assertCommandHandled(PatrolCommand.class);
        verify(spySquadron, times(1)).startPatrol(eq(PatrolType.OVERWORLD), any());
    }

    @Test
    void testPatrolWorldAlias() {
        addSquadronMember(0);
        send("#patrol o");

        sender.assertCommandHandled(PatrolCommand.class);
        verify(spySquadron, atLeastOnce()).startPatrol(eq(PatrolType.OVERWORLD), any());
    }

    @Test
    void testPatrolNotStarted_WhenAnotherPatrolActive() {
        addSquadronMember(0);

        send("#patrol o");
        verify(spySquadron, times(1)).startPatrol(any(), any());

        assertTrue(tester().getSquadron().getPatrol().isStarted());
        send("#patrol o");
        verify(spySquadron, times(1)).startPatrol(any(), any());
    }

    @Test
    void testCannotStopPatrol_whenNotActive() {
        assertFalse(tester().getSquadron().getPatrol().isStarted());
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
        assertTrue(tester().getSquadron().isEmpty());
        send("#squadronadd 12345678 asdfghjkl");
        assertTrue(tester().getSquadron().isEmpty());
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
        assertFalse(tester().getSquadron().getMembers().contains(getTesterCard(0)));
        assertFalse(tester().getSquadron().getMembers().contains(getTesterCard(1)));

        send("#squadronadd " + getTesterCard(0).getId() + " " + getTesterCard(1).getId());

        assertTrue(tester().getSquadron().getMembers().contains(getTesterCard(0)));
        assertTrue(tester().getSquadron().getMembers().contains(getTesterCard(1)));
    }

    @Test
    void testRemoveMembersFromSquadron() {
        addSquadronMember(0);
        addSquadronMember(1);
        addSquadronMember(3);

        assertTrue(tester().getSquadron().getMembers().contains(getTesterCard(0)));
        assertTrue(tester().getSquadron().getMembers().contains(getTesterCard(1)));

        send("#squadronremove " + getTesterCard(0).getId() + " " + getTesterCard(1).getId());

        assertFalse(tester().getSquadron().getMembers().contains(getTesterCard(0)));
        assertFalse(tester().getSquadron().getMembers().contains(getTesterCard(1)));
    }
}