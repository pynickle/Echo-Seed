package com.euphony.echoseed.fabric;

import com.euphony.echoseed.EchoItems;
import com.euphony.echoseed.EchoSeed;
import com.euphony.echoseed.network.EchoNetworking;
import com.euphony.echoseed.network.MarkSyncPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.item.CreativeModeTabs;

public final class EchoSeedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EchoFabricRegistries.register();
        EchoSeed.init();
        PayloadTypeRegistry.clientboundPlay().register(MarkSyncPayload.TYPE, MarkSyncPayload.STREAM_CODEC);
        EchoNetworking.sender = ServerPlayNetworking::send;
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS)
            .register(output -> output.accept(EchoItems.ECHO_SEED));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
            .register(output -> output.accept(EchoItems.ECHO_FRUIT));
    }
}
