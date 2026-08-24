package com.euphony.echoseed.neoforge;

import com.euphony.echoseed.EchoItems;
import com.euphony.echoseed.EchoSeed;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(EchoSeed.MOD_ID)
public final class EchoSeedNeoForge {
    public EchoSeedNeoForge(IEventBus modBus) {
        EchoSeed.init();
        modBus.addListener(EchoSeedNeoForge::addToNaturalBlocks);
    }

    private static void addToNaturalBlocks(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(EchoItems.ECHO_SEED);
        }
    }
}
