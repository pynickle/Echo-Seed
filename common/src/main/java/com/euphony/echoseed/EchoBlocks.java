package com.euphony.echoseed;

import com.euphony.echoseed.block.EchoCropBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public final class EchoBlocks {
    public static final EchoCropBlock ECHO_CROP = register(
        "echo_crop",
        EchoCropBlock::new,
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_CYAN)
            .noCollision()
            .instabreak()
            .sound(SoundType.CROP)
            .pushReaction(PushReaction.DESTROY)
            .noLootTable()
    );

    private EchoBlocks() {
    }

    private static <T extends Block> T register(
        String name,
        Function<BlockBehaviour.Properties, T> factory,
        BlockBehaviour.Properties properties
    ) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, EchoSeed.id(name));
        T block = factory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }
}
