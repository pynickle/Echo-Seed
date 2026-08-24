package com.euphony.echoseed.fabric.client;

import com.euphony.echoseed.EchoSeed;
import com.euphony.echoseed.client.EchoYaclScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public final class EchoModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!FabricLoader.getInstance().isModLoaded(EchoSeed.YACL_MOD_ID)) {
            return parent -> null;
        }
        return EchoYaclScreen::create;
    }
}
