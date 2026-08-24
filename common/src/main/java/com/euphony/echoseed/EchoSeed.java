package com.euphony.echoseed;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ComposterBlock;

public final class EchoSeed {
    public static final String MOD_ID = "echo_seed";
    private static final float VANILLA_SEED_COMPOST_CHANCE = 0.3F;

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void init() {
        ComposterBlock.COMPOSTABLES.put(EchoItems.ECHO_SEED, VANILLA_SEED_COMPOST_CHANCE);
    }

    private EchoSeed() {
    }
}
