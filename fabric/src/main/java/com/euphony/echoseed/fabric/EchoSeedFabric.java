package com.euphony.echoseed.fabric;

import com.euphony.echoseed.EchoItems;
import com.euphony.echoseed.EchoSeed;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.CreativeModeTabs;

public final class EchoSeedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EchoFabricRegistries.register();
        EchoSeed.init();
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS)
            .register(output -> output.accept(EchoItems.ECHO_SEED));
    }
}
