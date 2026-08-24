package com.euphony.echoseed.fabric;

import com.euphony.echoseed.EchoBlockEntityTypes;
import com.euphony.echoseed.EchoBlocks;
import com.euphony.echoseed.EchoItems;
import com.euphony.echoseed.EchoSeed;
import com.euphony.echoseed.block.EchoCropBlock;
import com.euphony.echoseed.block.EchoCropBlockEntity;
import com.euphony.echoseed.item.EchoFruitItem;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

final class EchoFabricRegistries {
    private EchoFabricRegistries() {
    }

    static void register() {
        EchoBlocks.ECHO_CROP = registerBlock(EchoBlocks.ECHO_CROP_ID, EchoCropBlock::new, EchoBlocks.cropProperties());
        EchoItems.ECHO_SEED = registerItem(
            EchoItems.ECHO_SEED_ID,
            properties -> new BlockItem(EchoBlocks.ECHO_CROP, properties),
            EchoItems.seedProperties()
        );
        EchoItems.ECHO_FRUIT = registerItem(
            EchoItems.ECHO_FRUIT_ID,
            EchoFruitItem::new,
            EchoItems.fruitProperties()
        );
        EchoBlockEntityTypes.ECHO_CROP = registerBlockEntity(
            EchoBlocks.ECHO_CROP_ID,
            EchoCropBlockEntity::new,
            EchoBlocks.ECHO_CROP
        );
    }

    private static <T extends Block> T registerBlock(
        String name,
        Function<BlockBehaviour.Properties, T> factory,
        BlockBehaviour.Properties properties
    ) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, EchoSeed.id(name));
        T block = factory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    private static Item registerItem(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, EchoSeed.id(name));
        Item item = factory.apply(properties.setId(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(
        String name,
        FabricBlockEntityTypeBuilder.Factory<? extends T> factory,
        Block... blocks
    ) {
        return Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            EchoSeed.id(name),
            FabricBlockEntityTypeBuilder.<T>create(factory, blocks).build()
        );
    }
}
