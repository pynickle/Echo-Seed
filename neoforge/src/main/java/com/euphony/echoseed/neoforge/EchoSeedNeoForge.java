package com.euphony.echoseed.neoforge;

import com.euphony.echoseed.EchoSeed;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(EchoSeed.MOD_ID)
public final class EchoSeedNeoForge {
    public EchoSeedNeoForge(IEventBus modBus) {
        EchoNeoForgeRegistries.register(modBus);
        modBus.addListener(EchoSeedNeoForge::commonSetup);
        modBus.addListener(EchoSeedNeoForge::addToCreativeTabs);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(EchoSeed::init);
    }

    private static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(EchoNeoForgeRegistries.ECHO_SEED);
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(EchoNeoForgeRegistries.ECHO_FRUIT);
        }
    }
}
