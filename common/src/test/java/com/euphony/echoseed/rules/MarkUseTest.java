package com.euphony.echoseed.rules;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarkUseTest {
    private final EchoRules rules = EchoRules.defaults();
    private final MarkLocation feet = new MarkLocation("minecraft:overworld", 10.5, 64.0, -3.25);
    private final MarkLocation nether = new MarkLocation("minecraft:the_nether", 20.0, 40.0, 8.0);

    @Test
    void useWithNoLiveMarkCreatesAMarkAtTheFeetAndDoesNotConsume() {
        MarkUseResult result = rules.useFruit(MarkState.none(), false, feet);

        assertEquals(FruitAction.CREATE_MARK, result.action());
        assertFalse(result.consumesFruit());
        assertFalse(result.showsCooldownHint());
        assertEquals(Optional.of(feet), result.location());
        assertTrue(result.state().hasLiveMark());
        LiveMark live = result.state().live().orElseThrow();
        assertEquals(feet, live.location());
        assertEquals(EchoRules.DEFAULT_MARK_DURATION_MILLIS, live.remainingMillis());
        assertEquals(0L, result.state().cooldownRemainingMillis());
    }

    @Test
    void sneakUseWithNoLiveMarkStillCreatesAMark() {
        MarkUseResult result = rules.useFruit(MarkState.none(), true, feet);

        assertEquals(FruitAction.CREATE_MARK, result.action());
        assertFalse(result.consumesFruit());
        assertTrue(result.state().hasLiveMark());
        assertEquals(feet, result.state().live().orElseThrow().location());
    }

    @Test
    void useWithALiveMarkTeleportsClearsAndConsumes() {
        MarkState marked = new MarkState(
            Optional.of(new LiveMark(nether, EchoRules.DEFAULT_MARK_DURATION_MILLIS)),
            0L
        );

        MarkUseResult result = rules.useFruit(marked, false, feet);

        assertEquals(FruitAction.TELEPORT, result.action());
        assertTrue(result.consumesFruit());
        assertFalse(result.showsCooldownHint());
        assertEquals(Optional.of(nether), result.location());
        assertFalse(result.state().hasLiveMark());
        assertEquals(EchoRules.DEFAULT_COOLDOWN_MILLIS, result.state().cooldownRemainingMillis());
    }

    @Test
    void sneakUseWithALiveMarkDismissesWithoutTeleportOrConsume() {
        MarkState marked = new MarkState(
            Optional.of(new LiveMark(feet, EchoRules.DEFAULT_MARK_DURATION_MILLIS)),
            0L
        );

        MarkUseResult result = rules.useFruit(marked, true, nether);

        assertEquals(FruitAction.DISMISS_MARK, result.action());
        assertFalse(result.consumesFruit());
        assertFalse(result.showsCooldownHint());
        assertEquals(Optional.empty(), result.location());
        assertFalse(result.state().hasLiveMark());
        assertEquals(0L, result.state().cooldownRemainingMillis());
    }

    @Test
    void anyFruitSharesTheSamePerPlayerMark() {
        MarkUseResult first = rules.useFruit(MarkState.none(), false, feet);
        MarkUseResult second = rules.useFruit(first.state(), false, nether);

        assertEquals(FruitAction.TELEPORT, second.action());
        assertEquals(Optional.of(feet), second.location());
        assertTrue(second.consumesFruit());
    }

    @Test
    void cooldownBlocksUseShowsAHintAndDoesNotConsume() {
        MarkState cooling = new MarkState(Optional.empty(), EchoRules.DEFAULT_COOLDOWN_MILLIS);

        MarkUseResult result = rules.useFruit(cooling, false, feet);

        assertEquals(FruitAction.BLOCKED_BY_COOLDOWN, result.action());
        assertTrue(result.showsCooldownHint());
        assertFalse(result.consumesFruit());
        assertFalse(result.state().hasLiveMark());
        assertEquals(EchoRules.DEFAULT_COOLDOWN_MILLIS, result.state().cooldownRemainingMillis());
        assertEquals(cooling, result.state());
    }

    @Test
    void creatingAMarkDoesNotStartCooldown() {
        MarkUseResult result = rules.useFruit(MarkState.none(), false, feet);

        assertEquals(0L, result.state().cooldownRemainingMillis());
    }

    @Test
    void dismissingAMarkDoesNotStartCooldown() {
        MarkState marked = new MarkState(
            Optional.of(new LiveMark(feet, EchoRules.DEFAULT_MARK_DURATION_MILLIS)),
            0L
        );

        MarkUseResult result = rules.useFruit(marked, true, feet);

        assertEquals(0L, result.state().cooldownRemainingMillis());
    }

    @Test
    void expiredMarkCreatesANewMarkOnUse() {
        MarkState expired = new MarkState(Optional.of(new LiveMark(feet, 0L)), 0L);

        MarkUseResult result = rules.useFruit(expired, false, nether);

        assertEquals(FruitAction.CREATE_MARK, result.action());
        assertFalse(result.consumesFruit());
        assertEquals(nether, result.state().live().orElseThrow().location());
    }

    @Test
    void teleportKeepsDimensionAndPosition() {
        MarkLocation home = new MarkLocation("minecraft:overworld", 1.0, 2.0, 3.0);
        MarkState marked = new MarkState(Optional.of(new LiveMark(home, 1_000L)), 0L);

        MarkUseResult result = rules.useFruit(marked, false, nether);

        assertEquals(Optional.of(home), result.location());
    }
}
