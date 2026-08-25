package com.euphony.echoseed.client;

import org.junit.jupiter.api.Test;

import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarkDurationDisplayTest {
    @Test
    void enabledDisplayRoundsRemainingDurationUpToWholeSeconds() {
        assertEquals(OptionalLong.of(45L), MarkDurationDisplay.remainingSeconds(true, 45_000L));
        assertEquals(OptionalLong.of(45L), MarkDurationDisplay.remainingSeconds(true, 44_001L));
        assertEquals(OptionalLong.of(1L), MarkDurationDisplay.remainingSeconds(true, 1L));
    }

    @Test
    void disabledOrExpiredDisplayIsHidden() {
        assertEquals(OptionalLong.empty(), MarkDurationDisplay.remainingSeconds(false, 45_000L));
        assertEquals(OptionalLong.empty(), MarkDurationDisplay.remainingSeconds(true, 0L));
    }
}
