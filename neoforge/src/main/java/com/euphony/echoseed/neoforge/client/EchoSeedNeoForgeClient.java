package com.euphony.echoseed.neoforge.client;

import com.euphony.echoseed.EchoSeed;
import com.euphony.echoseed.client.EchoMarkClient;
import com.euphony.echoseed.client.EchoYaclScreen;
import com.euphony.echoseed.config.EchoConfigs;
import com.euphony.echoseed.network.ConfigSyncPayload;
import com.euphony.echoseed.network.MarkSyncPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@Mod(value = EchoSeed.MOD_ID, dist = Dist.CLIENT)
public final class EchoSeedNeoForgeClient {
    public EchoSeedNeoForgeClient(IEventBus modBus) {
        modBus.addListener(EchoSeedNeoForgeClient::registerClientPayloads);
        if (ModList.get().isLoaded(EchoSeed.YACL_MOD_ID)) {
            ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> EchoYaclScreen.create(parent)
            );
        }
    }

    private static void registerClientPayloads(RegisterClientPayloadHandlersEvent event) {
        event.register(MarkSyncPayload.TYPE, (payload, context) -> EchoMarkClient.apply(payload));
        event.register(ConfigSyncPayload.TYPE, (payload, context) -> EchoConfigs.apply(payload.toConfig()));
    }
}
