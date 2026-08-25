package com.euphony.echoseed.client;

import com.euphony.echoseed.config.EchoConfigs;
import com.euphony.echoseed.network.MarkSyncPayload;
import com.euphony.echoseed.rules.EchoRules;
import com.euphony.echoseed.rules.LiveMark;
import com.euphony.echoseed.rules.MarkLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.random.RandomGenerator;

public final class EchoMarkClient {
    private static Optional<LiveMark> mark = Optional.empty();
    private static long remainingMillis;
    private static long lastClock = Long.MIN_VALUE;

    private EchoMarkClient() {
    }

    public static void apply(MarkSyncPayload payload) {
        Optional<LiveMark> next = payload.mark().filter(LiveMark::isLive);
        mark = next;
        remainingMillis = next.map(LiveMark::remainingMillis).orElse(0L);
        lastClock = Long.MIN_VALUE;
    }

    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null || mark.isEmpty()) {
            return;
        }
        long now = level.getOverworldClockTime();
        if (lastClock != Long.MIN_VALUE && now > lastClock) {
            remainingMillis = Math.max(0L, remainingMillis - (now - lastClock) * EchoRules.MILLIS_PER_GAME_TICK);
        }
        lastClock = now;
        if (remainingMillis <= 0L) {
            mark = Optional.empty();
            return;
        }
        if (minecraft.isPaused()) {
            return;
        }
        MarkLocation location = mark.get().location();
        for (MarkParticlePlan.Mote mote : MarkParticlePlan.motesForTick(
            true,
            remainingMillis,
            EchoConfigs.rules().markDurationMillis(),
            location.dimension(),
            level.dimension().identifier().toString(),
            location.x(),
            location.y(),
            location.z(),
            clientRandom(level.getRandom())
        )) {
            level.addParticle(
                new DustParticleOptions(mote.color(), mote.scale()),
                mote.x(),
                mote.y(),
                mote.z(),
                mote.vx(),
                mote.vy(),
                mote.vz()
            );
        }
    }

    private static RandomGenerator clientRandom(RandomSource random) {
        return new RandomGenerator() {
            @Override
            public long nextLong() {
                return random.nextLong();
            }

            @Override
            public double nextDouble() {
                return random.nextDouble();
            }
        };
    }
}
