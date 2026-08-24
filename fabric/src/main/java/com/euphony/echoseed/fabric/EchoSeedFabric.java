package com.euphony.echoseed.fabric;

import com.euphony.echoseed.EchoSeed;
import net.fabricmc.api.ModInitializer;

public final class EchoSeedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EchoSeed.init();
    }
}
