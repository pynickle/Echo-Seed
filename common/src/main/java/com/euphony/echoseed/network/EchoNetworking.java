package com.euphony.echoseed.network;

import net.minecraft.server.level.ServerPlayer;

public final class EchoNetworking {
    public static Sender sender = (player, payload) -> {
    };

    private EchoNetworking() {
    }

    public static void send(ServerPlayer player, MarkSyncPayload payload) {
        sender.send(player, payload);
    }

    @FunctionalInterface
    public interface Sender {
        void send(ServerPlayer player, MarkSyncPayload payload);
    }
}
