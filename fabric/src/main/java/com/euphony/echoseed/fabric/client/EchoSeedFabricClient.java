package com.euphony.echoseed.fabric.client;

import com.euphony.echoseed.client.EchoMarkClient;
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
    }
}
