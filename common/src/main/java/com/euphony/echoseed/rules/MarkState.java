package com.euphony.echoseed.rules;

import java.util.Optional;

public record MarkState(Optional<LiveMark> live, long cooldownRemainingMillis) {
    public static MarkState none() {
        return new MarkState(Optional.empty(), 0L);
    }

    public boolean hasLiveMark() {
        return live.filter(LiveMark::isLive).isPresent();
    }

    public Optional<MarkLocation> liveLocation() {
        return live.filter(LiveMark::isLive).map(LiveMark::location);
    }

    public MarkState elapse(long deltaMillis) {
        long delta = Math.max(0L, deltaMillis);
        Optional<LiveMark> remaining = live
            .map(mark -> new LiveMark(mark.location(), Math.max(0L, mark.remainingMillis() - delta)))
            .filter(LiveMark::isLive);
        return new MarkState(remaining, Math.max(0L, cooldownRemainingMillis - delta));
    }
}
