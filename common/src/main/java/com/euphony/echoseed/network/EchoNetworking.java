package com.euphony.echoseed.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public final class EchoNetworking {
    public static Sender sender = (player, payload) -> {
    };

    private EchoNetworking() {
    }

    public static void send(ServerPlayer player, CustomPacketPayload payload) {
        sender.send(player, payload);
    }

    @FunctionalInterface
    public interface Sender {
        void send(ServerPlayer player, CustomPacketPayload payload);
    }
}
