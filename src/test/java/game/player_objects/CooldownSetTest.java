package game.player_objects;

import bot.commands.user.shop.MessageSenderTester;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CooldownSetTest extends MessageSenderTester {

    @Test
    void testCooldownsCreatedAllowed() {
        CooldownSet cooldownSet = new CooldownSet();

        assertTrue(cooldownSet.getDrop().tryUse(Instant.now()));
        assertTrue(cooldownSet.getGrab().tryUse(Instant.now()));
    }

    @Test
    void testCooldownLockedAfterUse() {
        CooldownSet cooldownSet = new CooldownSet();

        Instant now = Instant.now();
        assertTrue(cooldownSet.getDrop().tryUse(now));
        assertFalse(cooldownSet.getDrop().tryUse(now));
    }

    @Test
    void testCooldownUnlockedAfterTimeout() {
        CooldownSet cooldownSet = new CooldownSet();

        Instant now = Instant.now();
        assertTrue(cooldownSet.getDrop().tryUse(now));
        assertTrue(cooldownSet.getDrop().tryUse(now.plusSeconds(cooldownSet.getDrop().getSecondsLeft(now) + 1)));
    }
}