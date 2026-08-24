package com.euphony.echoseed.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrowthTest {
    private final EchoRules rules = EchoRules.defaults();

    @Test
    void presenceForTwoMinutesAdvancesOneGrowthStage() {
        GrowthResult result = rules.grow(GrowthState.planted(), true, seconds(120));

        assertEquals(1, result.state().age());
        assertEquals(0L, result.state().presenceValueMillis());
        assertTrue(result.stageAdvanced());
    }

    @Test
    void presenceShortOfTwoMinutesDoesNotAdvance() {
        GrowthResult result = rules.grow(GrowthState.planted(), true, seconds(119));

        assertEquals(0, result.state().age());
        assertEquals(seconds(119), result.state().presenceValueMillis());
        assertFalse(result.stageAdvanced());
    }

    @Test
    void leftoverPresenceCarriesOntoTheNextStage() {
        GrowthResult result = rules.grow(GrowthState.planted(), true, seconds(150));

        assertEquals(1, result.state().age());
        assertEquals(seconds(30), result.state().presenceValueMillis());
        assertTrue(result.stageAdvanced());
    }

    @Test
    void sixMinutesOfPresenceFromPlantedReachesMature() {
        GrowthResult result = rules.grow(GrowthState.planted(), true, seconds(360));

        assertEquals(EchoRules.MATURE_AGE, result.state().age());
        assertEquals(0L, result.state().presenceValueMillis());
        assertTrue(result.stageAdvanced());
    }

    @Test
    void configuredShorterStageDurationFillsFaster() {
        EchoRules fast = new EchoRules(seconds(60), EchoRules.DEFAULT_MARK_DURATION_MILLIS, EchoRules.DEFAULT_COOLDOWN_MILLIS);

        GrowthResult result = fast.grow(GrowthState.planted(), true, seconds(60));

        assertEquals(1, result.state().age());
        assertEquals(0L, result.state().presenceValueMillis());
    }

    @Test
    void matureCropDoesNotAdvanceOrFill() {
        GrowthState mature = new GrowthState(EchoRules.MATURE_AGE, 0L);

        GrowthResult result = rules.grow(mature, true, seconds(120));

        assertEquals(EchoRules.MATURE_AGE, result.state().age());
        assertEquals(0L, result.state().presenceValueMillis());
        assertFalse(result.stageAdvanced());
    }

    @Test
    void matureCropDoesNotDecay() {
        GrowthState mature = new GrowthState(EchoRules.MATURE_AGE, seconds(60));

        GrowthResult result = rules.grow(mature, false, seconds(120));

        assertEquals(EchoRules.MATURE_AGE, result.state().age());
        assertEquals(seconds(60), result.state().presenceValueMillis());
        assertFalse(result.stageAdvanced());
    }

    @Test
    void absenceDecaysOnlyTheCurrentBarAtHalfFillRate() {
        GrowthState midStage = new GrowthState(1, seconds(60));

        GrowthResult result = rules.grow(midStage, false, seconds(80));

        assertEquals(1, result.state().age());
        assertEquals(seconds(20), result.state().presenceValueMillis());
        assertFalse(result.stageAdvanced());
    }

    @Test
    void decayDoesNotReduceGrowthStage() {
        GrowthState midStage = new GrowthState(2, seconds(10));

        GrowthResult result = rules.grow(midStage, false, seconds(120));

        assertEquals(2, result.state().age());
        assertEquals(0L, result.state().presenceValueMillis());
    }

    @Test
    void decayDoesNotGoBelowEmpty() {
        GrowthResult result = rules.grow(new GrowthState(0, seconds(5)), false, seconds(40));

        assertEquals(0, result.state().age());
        assertEquals(0L, result.state().presenceValueMillis());
    }

    private static long seconds(long count) {
        return count * 1000L;
    }
}
