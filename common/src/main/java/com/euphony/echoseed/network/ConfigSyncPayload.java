package com.euphony.echoseed.network;

import com.euphony.echoseed.EchoSeed;
import com.euphony.echoseed.rules.EchoConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ConfigSyncPayload(
        double growthSpeed,
        double markDurationSeconds,
        double cooldownSeconds,
        int presenceRange,
        boolean showMarkDuration
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ConfigSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(EchoSeed.id("config_sync"));
    public static final StreamCodec<FriendlyByteBuf, ConfigSyncPayload> STREAM_CODEC =
            CustomPacketPayload.codec(ConfigSyncPayload::write, ConfigSyncPayload::read);

    public static ConfigSyncPayload of(EchoConfig config) {
        return new ConfigSyncPayload(
                config.growthSpeed(),
                config.markDurationSeconds(),
                config.cooldownSeconds(),
                config.presenceRange(),
                config.showMarkDuration()
        );
    }

    public EchoConfig toConfig() {
        return EchoConfig.sanitize(growthSpeed, markDurationSeconds, cooldownSeconds, presenceRange, showMarkDuration);
    }

    private static ConfigSyncPayload read(FriendlyByteBuf input) {
        return new ConfigSyncPayload(
                input.readDouble(),
                input.readDouble(),
                input.readDouble(),
                input.readVarInt(),
                input.readBoolean()
        );
    }

    private void write(FriendlyByteBuf output) {
        output.writeDouble(growthSpeed);
        output.writeDouble(markDurationSeconds);
        output.writeDouble(cooldownSeconds);
        output.writeVarInt(presenceRange);
        output.writeBoolean(showMarkDuration);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
