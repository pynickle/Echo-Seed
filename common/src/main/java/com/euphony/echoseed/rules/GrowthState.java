package com.euphony.echoseed.rules;

public record GrowthState(int age, long presenceValueMillis) {
    public static GrowthState planted() {
        return new GrowthState(0, 0L);
    }
}
