package com.euphony.echoseed.neoforge;

import com.euphony.echoseed.EchoBlockEntityTypes;
import com.euphony.echoseed.EchoBlocks;
import com.euphony.echoseed.EchoItems;
import com.euphony.echoseed.EchoSeed;
import com.euphony.echoseed.block.EchoCropBlock;
import com.euphony.echoseed.block.EchoCropBlockEntity;
import com.euphony.echoseed.item.EchoFruitItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

final class EchoNeoForgeRegistries {
    static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(EchoSeed.MOD_ID);
    static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EchoSeed.MOD_ID);
    static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, EchoSeed.MOD_ID);

    static final DeferredBlock<EchoCropBlock> ECHO_CROP = BLOCKS.registerBlock(
        EchoBlocks.ECHO_CROP_ID,
        properties -> {
            EchoCropBlock block = new EchoCropBlock(properties);
            EchoBlocks.ECHO_CROP = block;
            return block;
        },
        EchoBlocks::cropProperties
    );

    static final DeferredItem<BlockItem> ECHO_SEED = ITEMS.registerItem(
        EchoItems.ECHO_SEED_ID,
        properties -> {
            BlockItem item = new BlockItem(ECHO_CROP.get(), properties);
            item.registerBlocks(Item.BY_BLOCK, item);
            EchoItems.ECHO_SEED = item;
            return item;
        },
        EchoItems::seedProperties
    );

    static final DeferredItem<Item> ECHO_FRUIT = ITEMS.registerItem(
        EchoItems.ECHO_FRUIT_ID,
        properties -> {
            Item item = new EchoFruitItem(properties);
            EchoItems.ECHO_FRUIT = item;
            return item;
        },
        EchoItems::fruitProperties
    );

    static final Supplier<BlockEntityType<EchoCropBlockEntity>> ECHO_CROP_ENTITY = BLOCK_ENTITY_TYPES.register(
        EchoBlocks.ECHO_CROP_ID,
        () -> {
            BlockEntityType<EchoCropBlockEntity> type = new BlockEntityType<>(
                EchoCropBlockEntity::new,
                false,
                ECHO_CROP.get()
            );
            EchoBlockEntityTypes.ECHO_CROP = type;
            return type;
        }
    );

    private EchoNeoForgeRegistries() {
    }

    static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITY_TYPES.register(modBus);
    }
}
