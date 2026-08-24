package com.euphony.echoseed.rules;

import java.util.List;
import java.util.OptionalInt;

public record CropLeaveResult(List<ItemDrop> drops, OptionalInt remainingAge) {
    public boolean cropRemains() {
        return remainingAge.isPresent();
    }
}
