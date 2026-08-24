package com.euphony.echoseed.rules;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.random.RandomGenerator;

/**
 * Pure Echo rules: Growth Stages and Presence Value, Echo Fruit Mark use, and crop leave drops.
 * No Minecraft types in this API.
 */
public final class EchoRules {
    public static final long DEFAULT_STAGE_DURATION_MILLIS = 120_000L;
    public static final long DEFAULT_MARK_DURATION_MILLIS = 45_000L;
    public static final long DEFAULT_COOLDOWN_MILLIS = 8_000L;
    public static final long MILLIS_PER_GAME_TICK = 50L;
    public static final int DEFAULT_PRESENCE_RANGE = 4;
    public static final int MATURE_AGE = 3;
    public static final int AGE_AFTER_PICK = 1;
    public static final int MIN_FRUIT = 2;
    public static final int MAX_FRUIT = 4;

    private final long stageDurationMillis;
    private final long markDurationMillis;
    private final long cooldownMillis;

    public EchoRules(long stageDurationMillis, long markDurationMillis, long cooldownMillis) {
        if (stageDurationMillis <= 0L) {
            throw new IllegalArgumentException("stageDurationMillis must be positive");
        }
        if (markDurationMillis <= 0L) {
            throw new IllegalArgumentException("markDurationMillis must be positive");
        }
        if (cooldownMillis < 0L) {
            throw new IllegalArgumentException("cooldownMillis must not be negative");
        }
        this.stageDurationMillis = stageDurationMillis;
        this.markDurationMillis = markDurationMillis;
        this.cooldownMillis = cooldownMillis;
    }

    public static EchoRules defaults() {
        return new EchoRules(
            DEFAULT_STAGE_DURATION_MILLIS,
            DEFAULT_MARK_DURATION_MILLIS,
            DEFAULT_COOLDOWN_MILLIS
        );
    }

    public static boolean isInsidePresenceRange(
        int cropX,
        int cropY,
        int cropZ,
        int playerX,
        int playerY,
        int playerZ,
        int range
    ) {
        int dx = Math.abs(playerX - cropX);
        int dy = Math.abs(playerY - cropY);
        int dz = Math.abs(playerZ - cropZ);
        return Math.max(dx, Math.max(dy, dz)) <= range;
    }

    public static boolean isEligiblePresent(boolean living, boolean spectator, boolean realConnectedPlayer) {
        return living && !spectator && realConnectedPlayer;
    }

    public static boolean anyonePresent(int cropX, int cropY, int cropZ, int range, Iterable<PresenceCandidate> candidates) {
        for (PresenceCandidate candidate : candidates) {
            if (isEligiblePresent(candidate.living(), candidate.spectator(), candidate.realConnectedPlayer())
                && isInsidePresenceRange(
                    cropX,
                    cropY,
                    cropZ,
                    candidate.blockX(),
                    candidate.blockY(),
                    candidate.blockZ(),
                    range
                )) {
                return true;
            }
        }
        return false;
    }

    public GrowthResult grow(GrowthState state, boolean anyonePresent, long deltaMillis) {
        if (state.age() >= MATURE_AGE) {
            return new GrowthResult(new GrowthState(MATURE_AGE, state.presenceValueMillis()), false);
        }
        long delta = Math.max(0L, deltaMillis);
        if (delta == 0L) {
            return new GrowthResult(state, false);
        }
        if (!anyonePresent) {
            long decayed = Math.max(0L, state.presenceValueMillis() - delta / 2L);
            return new GrowthResult(new GrowthState(state.age(), decayed), false);
        }
        long value = state.presenceValueMillis() + delta;
        int age = state.age();
        boolean advanced = false;
        while (age < MATURE_AGE && value >= stageDurationMillis) {
            value -= stageDurationMillis;
            age += 1;
            advanced = true;
        }
        if (age >= MATURE_AGE) {
            return new GrowthResult(new GrowthState(MATURE_AGE, 0L), true);
        }
        return new GrowthResult(new GrowthState(age, value), advanced);
    }

    public MarkUseResult useFruit(MarkState state, boolean sneaking, MarkLocation here) {
        if (state.cooldownRemainingMillis() > 0L) {
            return new MarkUseResult(FruitAction.BLOCKED_BY_COOLDOWN, state, Optional.empty());
        }
        if (state.hasLiveMark()) {
            if (sneaking) {
                return new MarkUseResult(
                    FruitAction.DISMISS_MARK,
                    new MarkState(Optional.empty(), state.cooldownRemainingMillis()),
                    Optional.empty()
                );
            }
            return new MarkUseResult(
                FruitAction.TELEPORT,
                new MarkState(Optional.empty(), cooldownMillis),
                state.liveLocation()
            );
        }
        LiveMark created = new LiveMark(here, markDurationMillis);
        return new MarkUseResult(
            FruitAction.CREATE_MARK,
            new MarkState(Optional.of(created), state.cooldownRemainingMillis()),
            Optional.of(here)
        );
    }

    public CropLeaveResult leave(int age, LeaveReason reason, RandomGenerator random) {
        if (reason == LeaveReason.PICK) {
            if (age < MATURE_AGE) {
                return new CropLeaveResult(List.of(), OptionalInt.of(age));
            }
            return new CropLeaveResult(
                List.of(echoFruit(random)),
                OptionalInt.of(AGE_AFTER_PICK)
            );
        }
        if (age >= MATURE_AGE) {
            return new CropLeaveResult(
                List.of(echoFruit(random), new ItemDrop(EchoItem.ECHO_SEED, 1)),
                OptionalInt.empty()
            );
        }
        return new CropLeaveResult(
            List.of(new ItemDrop(EchoItem.ECHO_SEED, 1)),
            OptionalInt.empty()
        );
    }

    private static ItemDrop echoFruit(RandomGenerator random) {
        return new ItemDrop(EchoItem.ECHO_FRUIT, MIN_FRUIT + random.nextInt(MAX_FRUIT - MIN_FRUIT + 1));
    }
}
