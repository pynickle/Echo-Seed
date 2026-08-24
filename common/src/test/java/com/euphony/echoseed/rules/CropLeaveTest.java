package com.euphony.echoseed.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.OptionalInt;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CropLeaveTest {
    private final EchoRules rules = EchoRules.defaults();

    @Test
    void pickAtMatureDropsFruitReturnsToStageOneAndYieldsNoSeed() {
        CropLeaveResult result = rules.leave(EchoRules.MATURE_AGE, LeaveReason.PICK, minFruit());

        assertEquals(OptionalInt.of(EchoRules.AGE_AFTER_PICK), result.remainingAge());
        assertTrue(result.cropRemains());
        assertEquals(2, fruitCount(result));
        assertEquals(0, seedCount(result));
    }

    @Test
    void pickAtMatureCanDropFourFruit() {
        CropLeaveResult result = rules.leave(EchoRules.MATURE_AGE, LeaveReason.PICK, maxFruit());

        assertEquals(4, fruitCount(result));
        assertEquals(0, seedCount(result));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void pickOnImmatureCropDoesNothing(int age) {
        CropLeaveResult result = rules.leave(age, LeaveReason.PICK, minFruit());

        assertEquals(OptionalInt.of(age), result.remainingAge());
        assertTrue(result.drops().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void breakOnImmatureCropDropsOneSeedAndRemovesTheCrop(int age) {
        CropLeaveResult result = rules.leave(age, LeaveReason.BREAK, minFruit());

        assertFalse(result.cropRemains());
        assertEquals(1, seedCount(result));
        assertEquals(0, fruitCount(result));
    }

    @Test
    void breakOnMatureDropsFruitPlusOneSeedAndRemovesTheCrop() {
        CropLeaveResult result = rules.leave(EchoRules.MATURE_AGE, LeaveReason.BREAK, minFruit());

        assertFalse(result.cropRemains());
        assertEquals(2, fruitCount(result));
        assertEquals(1, seedCount(result));
    }

    @ParameterizedTest
    @EnumSource(value = LeaveReason.class, names = {"WATER", "PISTON", "EXPLOSION"})
    void waterPistonAndExplosionUseTheSameDropsAsBreaking(LeaveReason reason) {
        CropLeaveResult immatureBreak = rules.leave(1, LeaveReason.BREAK, minFruit());
        CropLeaveResult immatureOther = rules.leave(1, reason, minFruit());
        CropLeaveResult matureBreak = rules.leave(EchoRules.MATURE_AGE, LeaveReason.BREAK, maxFruit());
        CropLeaveResult matureOther = rules.leave(EchoRules.MATURE_AGE, reason, maxFruit());

        assertEquals(immatureBreak, immatureOther);
        assertEquals(matureBreak, matureOther);
    }

    private static int fruitCount(CropLeaveResult result) {
        return result.drops().stream()
            .filter(drop -> drop.item() == EchoItem.ECHO_FRUIT)
            .mapToInt(ItemDrop::count)
            .sum();
    }

    private static int seedCount(CropLeaveResult result) {
        return result.drops().stream()
            .filter(drop -> drop.item() == EchoItem.ECHO_SEED)
            .mapToInt(ItemDrop::count)
            .sum();
    }

    private static RandomGenerator minFruit() {
        return new RandomGenerator() {
            @Override
            public long nextLong() {
                return 0L;
            }

            @Override
            public int nextInt(int bound) {
                return 0;
            }

            @Override
            public int nextInt(int origin, int bound) {
                return origin;
            }
        };
    }

    private static RandomGenerator maxFruit() {
        return new RandomGenerator() {
            @Override
            public long nextLong() {
                return Long.MAX_VALUE;
            }

            @Override
            public int nextInt(int bound) {
                return bound - 1;
            }

            @Override
            public int nextInt(int origin, int bound) {
                return bound - 1;
            }
        };
    }
}
