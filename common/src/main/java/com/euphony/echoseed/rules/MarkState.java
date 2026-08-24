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
}
