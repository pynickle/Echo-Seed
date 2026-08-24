package com.euphony.echoseed.config;

import com.euphony.echoseed.EchoPlatform;
import com.euphony.echoseed.network.ConfigSyncPayload;
import com.euphony.echoseed.network.EchoNetworking;
import com.euphony.echoseed.rules.EchoConfig;
import com.euphony.echoseed.rules.EchoRules;
import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class EchoConfigs {
    private static Path file = Path.of("config").resolve(EchoConfig.FILE_NAME);
    private static EchoConfig active = EchoConfig.DEFAULTS;
    private static EchoRules rules = EchoRules.defaults();

    private EchoConfigs() {
    }

    public static void load() {
        file = EchoPlatform.configDir().resolve(EchoConfig.FILE_NAME);
        apply(EchoConfig.read(file));
        if (!Files.isRegularFile(file)) {
            save();
        }
    }

    public static void reloadLocal() {
        apply(EchoConfig.read(file));
    }

    public static void apply(EchoConfig config) {
        active = config;
        rules = EchoRules.from(config);
    }

    public static void save() {
        try {
            EchoConfig.write(file, active);
        } catch (IOException ignored) {
        }
    }

    public static EchoConfig active() {
        return active;
    }

    public static EchoRules rules() {
        return rules;
    }

    public static void sync(ServerPlayer player) {
        EchoNetworking.send(player, ConfigSyncPayload.of(active));
    }

    public static void syncAll(Iterable<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            sync(player);
        }
    }
}
