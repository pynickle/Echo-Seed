package com.euphony.echoseed.client;

import com.euphony.echoseed.network.MarkSyncPayload;
import com.euphony.echoseed.rules.EchoRules;
import com.euphony.echoseed.rules.LiveMark;
import com.euphony.echoseed.rules.MarkLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public final class EchoMarkClient {
    private static final float PILLAR_HALF_WIDTH = 0.22F;
    private static final float PILLAR_HEIGHT = 1.8F;
    private static final float FOOTPRINT_HALF = 0.32F;
    private static final float FOOTPRINT_HEIGHT = 0.03F;
    private static final int GIZMO_LIFE_MILLIS = 80;

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

    public static void emitGizmos() {
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
        MarkLocation location = mark.get().location();
        if (!location.dimension().equals(level.dimension().identifier().toString())) {
            return;
        }
        float fade = (float) remainingMillis / (float) EchoRules.DEFAULT_MARK_DURATION_MILLIS;
        int rgb = DustColorTransitionOptions.SCULK_PARTICLE_COLOR;
        int pillarFill = ARGB.color((int) (90 * fade), rgb);
        int pillarStroke = ARGB.color((int) (140 * fade), rgb);
        int footprintFill = ARGB.color((int) (70 * fade), rgb);
        Vec3 feet = new Vec3(location.x(), location.y(), location.z());
        AABB pillar = new AABB(
            feet.x - PILLAR_HALF_WIDTH,
            feet.y,
            feet.z - PILLAR_HALF_WIDTH,
            feet.x + PILLAR_HALF_WIDTH,
            feet.y + PILLAR_HEIGHT,
            feet.z + PILLAR_HALF_WIDTH
        );
        AABB footprint = new AABB(
            feet.x - FOOTPRINT_HALF,
            feet.y,
            feet.z - FOOTPRINT_HALF,
            feet.x + FOOTPRINT_HALF,
            feet.y + FOOTPRINT_HEIGHT,
            feet.z + FOOTPRINT_HALF
        );
        Gizmos.cuboid(pillar, GizmoStyle.strokeAndFill(pillarStroke, 1.5F, pillarFill)).persistForMillis(GIZMO_LIFE_MILLIS);
        Gizmos.cuboid(footprint, GizmoStyle.fill(footprintFill)).persistForMillis(GIZMO_LIFE_MILLIS);
    }
}
