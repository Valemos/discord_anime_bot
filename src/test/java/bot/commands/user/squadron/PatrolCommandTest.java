package bot.commands.user.squadron;

import bot.commands.AbstractCommandTest;
import bot.commands.arguments.MultipleIdentifiersArguments;
import game.cards.CardPersonal;
import game.player_objects.squadron.PatrolType;
import game.player_objects.squadron.Squadron;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class PatrolCommandTest extends AbstractCommandTest<PatrolCommand, PatrolCommand.Arguments> {

    SquadronRemoveCommand cmdSquadronRemove;
    private SquadronAddCommand cmdSquadronAdd;

    @BeforeEach
    void setUp() {
        setCommand(new PatrolCommand(spyGame));

        cmdSquadronRemove = new SquadronRemoveCommand(spyGame);
        cmdSquadronAdd = new SquadronAddCommand(spyGame);
    }

    private void addSquadronMember(int index) {
        CardPersonal card = tester.getCards().get(index);
        spyGame.addSquadronMember(tester, card);
        reset(spyGame);
    }

    @Test
    void testPatrolNotStartedForEmptySquadron() {
        getTesterSquadron().setMembers(new HashSet<>());

        arguments.patrolType = PatrolType.OVERWORLD;
        handleCommand();

        verify(spyGame, never()).createNewPatrol(any(), any(), any());
    }

    @Test
    void testPatrolStartedFullName() {
        addSquadronMember(0);

        arguments.setPatrolWorld(PatrolType.OVERWORLD);
        handleCommand();

        verify(spyGame, times(1)).createNewPatrol(any(), eq(PatrolType.OVERWORLD), any());
    }

    @Test
    void testPatrolWorldAlias() {
        addSquadronMember(0);

        arguments.setPatrolWorld(PatrolType.O);
        handleCommand();

        verify(spyGame, atLeastOnce()).createNewPatrol(any(), eq(PatrolType.OVERWORLD), any());
    }

    @Test
    void testPatrolNotStarted_WhenAnotherPatrolActive() {
        addSquadronMember(0);

        arguments.setPatrolWorld(PatrolType.O);
        handleCommand();

        verify(spyGame, times(1)).createNewPatrol(any(), any(), any());
        assertTrue(tester.getSquadron().getPatrol().isBusy());

        handleCommand(arguments);
        verify(spyGame, times(1)).createNewPatrol(any(), any(), any());
    }

    @Test
    void testCannotStopPatrol_whenNotActive() {
        assertFalse(getTesterSquadron().getPatrol().isBusy());
        handleCommand(new PatrolStopCommand(spyGame));
        verify(spyGame, never()).finishPatrol(any(), any());
    }

    @Test
    void testPatrolStopped_whenActive() {
        addSquadronMember(0);

        arguments.setPatrolWorld(PatrolType.O);
        handleCommand();

        assertTrue(tester.getSquadron().getPatrol().isBusy());

        handleCommand(new PatrolStopCommand(spyGame));
        verify(spyGame).finishPatrol(any(), any());
    }

    @Test
    void testAddNotExistingMembersToSquadron() {
        assertTrue(getTesterSquadron().isEmpty());

        MultipleIdentifiersArguments args = createArguments(cmdSquadronAdd);

        args.multipleIds.add("12345678");
        args.multipleIds.add("asdfghjkl");
        handleCommand(cmdSquadronAdd, tester, args);

        assertTrue(getTesterSquadron().isEmpty());
    }

    @Test
    void testRemoveNotExistingFromSquadron() {
        addSquadronMember(0);
        assertEquals(1, tester.getSquadron().getMembers().size());

        MultipleIdentifiersArguments args = createArguments(cmdSquadronRemove);

        args.multipleIds.add("12345678");
        args.multipleIds.add("asdfghjkl");
        handleCommand(cmdSquadronRemove, tester, args);

        assertEquals(1, tester.getSquadron().getMembers().size());
    }

    @Test
    void testCannotAddToSquadron_WhenInPatrol() {
        addSquadronMember(0);

        arguments.setPatrolWorld(PatrolType.O);
        handleCommand();

        MultipleIdentifiersArguments args = createArguments(cmdSquadronAdd);

        args.multipleIds.add("12345678");
        args.multipleIds.add(tester.getCards().get(0).getId());
        handleCommand(cmdSquadronAdd, tester, args);

        verify(spyGame, never()).addSquadronMember(any(), any());
    }

    @Test
    void testCannotRemoveFromSquadron_WhenInPatrol() {
        addSquadronMember(0);

        arguments.setPatrolWorld(PatrolType.O);
        handleCommand();

        MultipleIdentifiersArguments args = createArguments(cmdSquadronRemove);
        args.multipleIds.add("1234567890");
        args.multipleIds.add(tester.getCards().get(0).getId());

        handleCommand(cmdSquadronRemove, tester, args);

        verify(spyGame, never()).removeSquadronMembers(any(), any());
    }

    @Test
    void testAddMembersToSquadron() {
        CardPersonal card1 = tester.getCards().get(0);
        CardPersonal card2 = tester.getCards().get(1);
        assertFalse(getTesterSquadron().getMembers().contains(card1));
        assertFalse(getTesterSquadron().getMembers().contains(card2));

        MultipleIdentifiersArguments args = createArguments(cmdSquadronAdd);
        args.multipleIds.add(card1.getId());
        args.multipleIds.add(card2.getId());
        handleCommand(cmdSquadronAdd, tester, args);

        assertTrue(getTesterSquadron().getMembers().contains(card1));
        assertTrue(getTesterSquadron().getMembers().contains(card2));
    }

    @Test
    void testRemoveMembersFromSquadron() {
        addSquadronMember(0);
        addSquadronMember(1);
        addSquadronMember(3);

        CardPersonal card1 = tester.getCards().get(0);
        CardPersonal card2 = tester.getCards().get(1);

        assertTrue(getTesterSquadron().getMembers().contains(card1));
        assertTrue(getTesterSquadron().getMembers().contains(card2));

        MultipleIdentifiersArguments args = createArguments(cmdSquadronRemove);
        args.multipleIds.add(card1.getId());
        args.multipleIds.add(card2.getId());
        handleCommand(cmdSquadronRemove, tester, args);

        assertFalse(getTesterSquadron().getMembers().contains(card1));
        assertFalse(getTesterSquadron().getMembers().contains(card2));
    }

    @Test
    void testEditSquadronAfterFinishedPatrol() {
        addSquadronMember(0);

        arguments.setPatrolWorld(PatrolType.O);
        handleCommand();

        handleCommand(new PatrolStopCommand(spyGame));

        MultipleIdentifiersArguments args = createArguments(cmdSquadronRemove);
        args.multipleIds.add("1234567890");
        args.multipleIds.add(tester.getCards().get(0).getId());
        handleCommand(cmdSquadronRemove, tester, args);

        verify(spyGame, never()).removeSquadronMembers(any(), any());
    }

    @NotNull
    private Squadron getTesterSquadron() {
        return spyGame.getOrCreateSquadron(tester);
    }
}