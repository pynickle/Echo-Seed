package com.euphony.echoseed;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public final class EchoItems {
    public static final Item ECHO_SEED = register(
        "echo_seed",
        properties -> new BlockItem(EchoBlocks.ECHO_CROP, properties),
        new Item.Properties().useItemDescriptionPrefix()
    );

    private EchoItems() {
    }

    private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, EchoSeed.id(name));
        Item item = factory.apply(properties.setId(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}
