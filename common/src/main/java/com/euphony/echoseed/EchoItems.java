package com.euphony.echoseed;

import com.euphony.echoseed.rules.EchoItem;
import com.euphony.echoseed.rules.ItemDrop;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class EchoItems {
    public static final String ECHO_SEED_ID = "echo_seed";
    public static final String ECHO_FRUIT_ID = "echo_fruit";
    public static Item ECHO_SEED;
    public static Item ECHO_FRUIT;

    private EchoItems() {
    }

    public static Item.Properties seedProperties() {
        return new Item.Properties().useItemDescriptionPrefix();
    }

    public static Item.Properties fruitProperties() {
        return new Item.Properties();
    }

    public static Item of(EchoItem item) {
        return switch (item) {
            case ECHO_SEED -> ECHO_SEED;
            case ECHO_FRUIT -> ECHO_FRUIT;
        };
    }

    public static ItemStack stack(ItemDrop drop) {
        return new ItemStack(of(drop.item()), drop.count());
    }
}
