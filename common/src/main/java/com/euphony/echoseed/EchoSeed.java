package com.euphony.echoseed;

import com.euphony.echoseed.config.EchoConfigs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ComposterBlock;

public final class EchoSeed {
    public static final String MOD_ID = "echo_seed";
    public static final String YACL_MOD_ID = "yet_another_config_lib_v3";
    private static final float VANILLA_SEED_COMPOST_CHANCE = 0.3F;

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void init() {
        EchoConfigs.load();
        ComposterBlock.COMPOSTABLES.put(EchoItems.ECHO_SEED, VANILLA_SEED_COMPOST_CHANCE);
    }

    private EchoSeed() {
    }
}
