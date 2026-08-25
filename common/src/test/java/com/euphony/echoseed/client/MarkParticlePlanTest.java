package com.euphony.echoseed.client;

import com.euphony.echoseed.rules.EchoRules;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarkParticlePlanTest {
    private static final String OVERWORLD = "minecraft:overworld";
    private static final double FEET_X = 10.5;
    private static final double FEET_Y = 64.0;
    private static final double FEET_Z = -3.25;

    @Test
    void noLiveMarkEmitsNoMotes() {
        assertTrue(plan(
            false,
            EchoRules.DEFAULT_MARK_DURATION_MILLIS,
            EchoRules.DEFAULT_MARK_DURATION_MILLIS,
            OVERWORLD,
            OVERWORLD,
            unusedRandom()
        ).isEmpty());
    }

    @Test
    void fullRemainingInTheMatchingDimensionEmitsTwoMotes() {
        assertEquals(
            2,
            plan(
                true,
                EchoRules.DEFAULT_MARK_DURATION_MILLIS,
                EchoRules.DEFAULT_MARK_DURATION_MILLIS,
                OVERWORLD,
                OVERWORLD,
                new Random(1L)
            ).size()
        );
    }

    @Test
    void expiredRemainingEmitsNoMotes() {
        assertTrue(plan(
            true,
            0L,
            EchoRules.DEFAULT_MARK_DURATION_MILLIS,
            OVERWORLD,
            OVERWORLD,
            unusedRandom()
        ).isEmpty());
    }

    @Test
    void wrongDimensionEmitsNoMotes() {
        assertTrue(plan(
            true,
            EchoRules.DEFAULT_MARK_DURATION_MILLIS,
            EchoRules.DEFAULT_MARK_DURATION_MILLIS,
            OVERWORLD,
            "minecraft:the_nether",
            unusedRandom()
        ).isEmpty());
    }

    @Test
    void halfRemainingEmitsOneMote() {
        assertEquals(
            1,
            plan(
                true,
                EchoRules.DEFAULT_MARK_DURATION_MILLIS / 2L,
                EchoRules.DEFAULT_MARK_DURATION_MILLIS,
                OVERWORLD,
                OVERWORLD,
                new Random(1L)
            ).size()
        );
    }

    @Test
    void configuredDurationScalesTheMoteCount() {
        assertEquals(
            1,
            plan(true, 60_000L, 120_000L, OVERWORLD, OVERWORLD, new Random(1L)).size()
        );
    }

    @Test
    void motesSpawnInsideTheColumn() {
        boolean lifted = false;
        boolean offAxis = false;
        for (long seed = 1L; seed <= 20L; seed++) {
            List<MarkParticlePlan.Mote> motes = plan(
                true,
                EchoRules.DEFAULT_MARK_DURATION_MILLIS,
                EchoRules.DEFAULT_MARK_DURATION_MILLIS,
                OVERWORLD,
                OVERWORLD,
                new Random(seed)
            );
            assertEquals(2, motes.size());
            for (MarkParticlePlan.Mote mote : motes) {
                double dx = mote.x() - FEET_X;
                double dz = mote.z() - FEET_Z;
                double radial = Math.hypot(dx, dz);
                assertTrue(radial <= 0.35, "radial " + radial);
                assertTrue(mote.y() >= FEET_Y, "y " + mote.y());
                assertTrue(mote.y() <= FEET_Y + 2.0, "y " + mote.y());
                if (mote.y() > FEET_Y + 0.1) {
                    lifted = true;
                }
                if (radial > 0.05) {
                    offAxis = true;
                }
            }
        }
        assertTrue(lifted);
        assertTrue(offAxis);
    }

    @Test
    void motesUsePresenceTealDust() {
        MarkParticlePlan.Mote mote = plan(
            true,
            EchoRules.DEFAULT_MARK_DURATION_MILLIS,
            EchoRules.DEFAULT_MARK_DURATION_MILLIS,
            OVERWORLD,
            OVERWORLD,
            new Random(1L)
        ).getFirst();

        assertEquals(0x009295, mote.color());
        assertEquals(1.0F, mote.scale());
    }

    @Test
    void motesRise() {
        MarkParticlePlan.Mote mote = plan(
            true,
            EchoRules.DEFAULT_MARK_DURATION_MILLIS,
            EchoRules.DEFAULT_MARK_DURATION_MILLIS,
            OVERWORLD,
            OVERWORLD,
            new Random(1L)
        ).getFirst();

        assertTrue(mote.vy() > 0.0);
    }

    private static List<MarkParticlePlan.Mote> plan(
        boolean live,
        long remainingMillis,
        long durationMillis,
        String markDimension,
        String currentDimension,
        RandomGenerator random
    ) {
        return MarkParticlePlan.motesForTick(
            live,
            remainingMillis,
            durationMillis,
            markDimension,
            currentDimension,
            FEET_X,
            FEET_Y,
            FEET_Z,
            random
        );
    }

    private static RandomGenerator unusedRandom() {
        return new RandomGenerator() {
            @Override
            public long nextLong() {
                throw new AssertionError("random should not be used");
            }
        };
    }
}
