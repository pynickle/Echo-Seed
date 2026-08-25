package com.euphony.echoseed;

import com.euphony.echoseed.block.EchoCropBlock;
import com.euphony.echoseed.rules.EchoRules;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class EchoBlocks {
    public static final String ECHO_CROP_ID = "echo_crop";
    public static EchoCropBlock ECHO_CROP;

    private EchoBlocks() {
    }

    public static BlockBehaviour.Properties cropProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_CYAN)
                .noCollision()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .lightLevel(state -> state.getValue(EchoCropBlock.AGE) == EchoRules.MATURE_AGE ? 4 : 0);
    }
}
