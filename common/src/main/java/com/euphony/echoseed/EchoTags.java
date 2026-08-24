package com.euphony.echoseed;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class EchoTags {
    public static final TagKey<Block> PLANTABLE_SOIL = TagKey.create(Registries.BLOCK, EchoSeed.id("plantable_soil"));

    private EchoTags() {
    }
}
