package com.euphony.echoseed.fabric.client;

import com.euphony.echoseed.client.EchoMarkClient;
import com.euphony.echoseed.config.EchoConfigs;
import com.euphony.echoseed.network.ConfigSyncPayload;
import com.euphony.echoseed.network.MarkSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class EchoSeedFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
            MarkSyncPayload.TYPE,
            (payload, context) -> EchoMarkClient.apply(payload)
        );
        ClientPlayNetworking.registerGlobalReceiver(
            ConfigSyncPayload.TYPE,
            (payload, context) -> EchoConfigs.apply(payload.toConfig())
        );
    }
}
