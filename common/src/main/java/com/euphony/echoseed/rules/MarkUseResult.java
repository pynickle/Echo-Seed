package com.euphony.echoseed.rules;

import java.util.Optional;

public record MarkUseResult(FruitAction action, MarkState state, Optional<MarkLocation> location) {
    public boolean consumesFruit() {
        return action == FruitAction.TELEPORT;
    }

    public boolean showsCooldownHint() {
        return action == FruitAction.BLOCKED_BY_COOLDOWN;
    }
}
