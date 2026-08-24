package com.euphony.echoseed.neoforge.client;

import com.euphony.echoseed.EchoSeed;
import com.euphony.echoseed.client.EchoMarkClient;
import com.euphony.echoseed.network.MarkSyncPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@Mod(value = EchoSeed.MOD_ID, dist = Dist.CLIENT)
public final class EchoSeedNeoForgeClient {
    public EchoSeedNeoForgeClient(IEventBus modBus) {
        modBus.addListener(EchoSeedNeoForgeClient::registerClientPayloads);
    }

    private static void registerClientPayloads(RegisterClientPayloadHandlersEvent event) {
        event.register(MarkSyncPayload.TYPE, (payload, context) -> EchoMarkClient.apply(payload));
    }
}
