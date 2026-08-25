package com.euphony.echoseed.mark;

import com.euphony.echoseed.network.EchoNetworking;
import com.euphony.echoseed.network.MarkSyncPayload;
import com.euphony.echoseed.rules.EchoRules;
import com.euphony.echoseed.rules.LiveMark;
import com.euphony.echoseed.rules.MarkLocation;
import com.euphony.echoseed.rules.MarkState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class EchoMarks {
    private static final String MARK_KEY = "echo_seed_mark";
    private static final String DIMENSION_KEY = "Dimension";
    private static final String X_KEY = "X";
    private static final String Y_KEY = "Y";
    private static final String Z_KEY = "Z";
    private static final String REMAINING_KEY = "Remaining";
    private static final String COOLDOWN_KEY = "echo_seed_cooldown";
    private static final String CLOCK_KEY = "echo_seed_clock";

    private static final Map<UUID, Stored> BY_PLAYER = new HashMap<>();

    private EchoMarks() {
    }

    public static MarkState get(ServerPlayer player) {
        tick(player);
        Stored stored = BY_PLAYER.get(player.getUUID());
        return stored == null ? MarkState.none() : stored.state();
    }

    public static void set(ServerPlayer player, MarkState state) {
        BY_PLAYER.put(player.getUUID(), new Stored(state, clock(player)));
        sync(player);
    }

    public static void tick(ServerPlayer player) {
        Stored stored = BY_PLAYER.get(player.getUUID());
        if (stored == null) {
            return;
        }
        long now = clock(player);
        long deltaMillis = Math.max(0L, now - stored.clock()) * EchoRules.MILLIS_PER_GAME_TICK;
        MarkState next = stored.state().elapse(deltaMillis);
        if (next.equals(stored.state()) && now == stored.clock()) {
            return;
        }
        boolean expired = stored.state().hasLiveMark() && !next.hasLiveMark();
        BY_PLAYER.put(player.getUUID(), new Stored(next, now));
        if (expired) {
            sync(player);
        }
    }

    public static void sync(ServerPlayer player) {
        Stored stored = BY_PLAYER.get(player.getUUID());
        Optional<LiveMark> mark = stored == null ? Optional.empty() : stored.state().live().filter(LiveMark::isLive);
        EchoNetworking.send(player, new MarkSyncPayload(mark));
    }

    public static void save(ServerPlayer player, ValueOutput output) {
        tick(player);
        Stored stored = BY_PLAYER.get(player.getUUID());
        if (stored == null) {
            return;
        }
        MarkState state = stored.state();
        state.live().filter(LiveMark::isLive).ifPresent(live -> {
            ValueOutput mark = output.child(MARK_KEY);
            MarkLocation location = live.location();
            mark.putString(DIMENSION_KEY, location.dimension());
            mark.putDouble(X_KEY, location.x());
            mark.putDouble(Y_KEY, location.y());
            mark.putDouble(Z_KEY, location.z());
            mark.putLong(REMAINING_KEY, live.remainingMillis());
        });
        if (state.cooldownRemainingMillis() > 0L) {
            output.putLong(COOLDOWN_KEY, state.cooldownRemainingMillis());
        }
        output.putLong(CLOCK_KEY, stored.clock());
    }

    public static void load(ServerPlayer player, ValueInput input) {
        Optional<LiveMark> live = input.child(MARK_KEY).flatMap(mark -> {
            Optional<String> dimension = mark.getString(DIMENSION_KEY);
            Optional<Long> remaining = mark.getLong(REMAINING_KEY);
            if (dimension.isEmpty() || remaining.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new LiveMark(
                    new MarkLocation(
                            dimension.get(),
                            mark.getDoubleOr(X_KEY, 0.0),
                            mark.getDoubleOr(Y_KEY, 0.0),
                            mark.getDoubleOr(Z_KEY, 0.0)
                    ),
                    remaining.get()
            ));
        });
        long cooldown = input.getLongOr(COOLDOWN_KEY, 0L);
        long storedClock = input.getLongOr(CLOCK_KEY, clock(player));
        BY_PLAYER.put(player.getUUID(), new Stored(new MarkState(live, cooldown), storedClock));
    }

    public static void forget(ServerPlayer player) {
        BY_PLAYER.remove(player.getUUID());
    }

    private static long clock(ServerPlayer player) {
        return player.level().getOverworldClockTime();
    }

    private record Stored(MarkState state, long clock) {
    }
}
