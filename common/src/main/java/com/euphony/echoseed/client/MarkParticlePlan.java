package com.euphony.echoseed.client;

import java.util.List;
import java.util.random.RandomGenerator;

public final class MarkParticlePlan {
    private static final int COLOR = 0x009295;
    private static final float SCALE = 1.0F;

    private static final double HEIGHT = 2.0;
    private static final double RADIUS = 0.35;
    private static final double RISE = 0.25;

    private MarkParticlePlan() {
    }

    public record Mote(
        double x,
        double y,
        double z,
        int color,
        float scale,
        double vx,
        double vy,
        double vz
    ) {
    }

    public static List<Mote> motesForTick(
        boolean live,
        long remainingMillis,
        long durationMillis,
        String markDimension,
        String currentDimension,
        double x,
        double y,
        double z,
        RandomGenerator random
    ) {
        if (!live || remainingMillis <= 0L || !markDimension.equals(currentDimension)) {
            return List.of();
        }
        long duration = Math.max(1L, durationMillis);
        int count = (int) Math.round(2.0 * remainingMillis / (double) duration);
        if (count <= 0) {
            return List.of();
        }
        Mote[] motes = new Mote[count];
        for (int i = 0; i < count; i++) {
            motes[i] = spawn(x, y, z, random);
        }
        return List.of(motes);
    }

    private static Mote spawn(double x, double y, double z, RandomGenerator random) {
        double theta = random.nextDouble() * Math.PI * 2.0;
        double radial = RADIUS * Math.sqrt(random.nextDouble());
        return new Mote(
            x + Math.cos(theta) * radial,
            y + random.nextDouble() * HEIGHT,
            z + Math.sin(theta) * radial,
            COLOR,
            SCALE,
            (random.nextDouble() - 0.5) * 0.04,
            RISE + random.nextDouble() * 0.05,
            (random.nextDouble() - 0.5) * 0.04
        );
    }
}
