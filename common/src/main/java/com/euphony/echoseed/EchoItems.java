package com.euphony.echoseed;

import net.minecraft.world.item.Item;

public final class EchoItems {
    public static final String ECHO_SEED_ID = "echo_seed";
    public static Item ECHO_SEED;

    private EchoItems() {
    }

    public static Item.Properties seedProperties() {
        return new Item.Properties().useItemDescriptionPrefix();
    }
}
