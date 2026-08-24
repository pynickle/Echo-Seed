package com.euphony.echoseed.rules;

public record LiveMark(MarkLocation location, long remainingMillis) {
    public boolean isLive() {
        return remainingMillis > 0L;
    }
}
