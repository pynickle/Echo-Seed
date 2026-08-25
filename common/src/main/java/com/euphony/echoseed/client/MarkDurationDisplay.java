package com.euphony.echoseed.client;

import java.util.OptionalLong;

public final class MarkDurationDisplay {
    private MarkDurationDisplay() {
    }

    public static OptionalLong remainingSeconds(boolean enabled, long remainingMillis) {
        if (!enabled || remainingMillis <= 0L) {
            return OptionalLong.empty();
        }
        return OptionalLong.of(Math.ceilDiv(remainingMillis, 1000L));
    }
}
