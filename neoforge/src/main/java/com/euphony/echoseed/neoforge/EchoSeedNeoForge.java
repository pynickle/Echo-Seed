package com.euphony.echoseed.neoforge;

import com.euphony.echoseed.EchoSeed;
import com.euphony.echoseed.network.EchoNetworking;
import com.euphony.echoseed.network.MarkSyncPayload;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(EchoSeed.MOD_ID)
public final class EchoSeedNeoForge {
    public EchoSeedNeoForge(IEventBus modBus) {
        EchoNeoForgeRegistries.register(modBus);
        EchoNetworking.sender = (player, payload) -> PacketDistributor.sendToPlayer(player, payload);
        modBus.addListener(EchoSeedNeoForge::commonSetup);
        modBus.addListener(EchoSeedNeoForge::addToCreativeTabs);
        modBus.addListener(EchoSeedNeoForge::registerPayloads);
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

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(MarkSyncPayload.TYPE, MarkSyncPayload.STREAM_CODEC);
    }
}
