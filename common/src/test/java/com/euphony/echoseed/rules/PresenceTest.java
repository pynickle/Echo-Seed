package com.euphony.echoseed.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PresenceTest {
    @ParameterizedTest
    @CsvSource({
        "0, 0, 0, true",
        "4, 0, 0, true",
        "-4, 0, 0, true",
        "0, 4, 0, true",
        "0, 0, 4, true",
        "4, 4, 4, true",
        "5, 0, 0, false",
        "0, 5, 0, false",
        "0, 0, -5, false",
        "4, 4, 5, false"
    })
    void presenceRangeIsAChebyshevCubeOfExtentFour(int dx, int dy, int dz, boolean inside) {
        boolean result = EchoRules.isInsidePresenceRange(10, 64, -3, 10 + dx, 64 + dy, -3 + dz, EchoRules.DEFAULT_PRESENCE_RANGE);

        assertEquals(inside, result);
    }

    @Test
    void livingRealPlayerWhoIsNotASpectatorIsEligible() {
        assertTrue(EchoRules.isEligiblePresent(true, false, true));
    }

    @Test
    void spectatorIsNotEligibleEvenIfLivingAndReal() {
        assertFalse(EchoRules.isEligiblePresent(true, true, true));
    }

    @Test
    void deadPlayerIsNotEligible() {
        assertFalse(EchoRules.isEligiblePresent(false, false, true));
    }

    @Test
    void fakePlayerIsNotEligible() {
        assertFalse(EchoRules.isEligiblePresent(true, false, false));
    }

    @Test
    void oneEligiblePlayerInsideRangeIsEnough() {
        PresenceCandidate present = new PresenceCandidate(10, 64, -3, true, false, true);
        PresenceCandidate farAway = new PresenceCandidate(80, 64, -3, true, false, true);

        assertTrue(EchoRules.anyonePresent(10, 64, -3, EchoRules.DEFAULT_PRESENCE_RANGE, List.of(farAway, present)));
    }

    @Test
    void twoEligiblePlayersInsideRangeStillCountAsOnePresence() {
        PresenceCandidate first = new PresenceCandidate(10, 64, -3, true, false, true);
        PresenceCandidate second = new PresenceCandidate(12, 65, -2, true, false, true);

        assertTrue(EchoRules.anyonePresent(10, 64, -3, EchoRules.DEFAULT_PRESENCE_RANGE, List.of(first, second)));
        assertTrue(EchoRules.anyonePresent(10, 64, -3, EchoRules.DEFAULT_PRESENCE_RANGE, List.of(first)));
    }

    @Test
    void crowdOfIneligiblePlayersDoesNotCountAsPresent() {
        PresenceCandidate spectator = new PresenceCandidate(10, 64, -3, true, true, true);
        PresenceCandidate dead = new PresenceCandidate(11, 64, -3, false, false, true);
        PresenceCandidate fake = new PresenceCandidate(12, 64, -3, true, false, false);

        assertFalse(EchoRules.anyonePresent(10, 64, -3, EchoRules.DEFAULT_PRESENCE_RANGE, List.of(spectator, dead, fake)));
    }
}
