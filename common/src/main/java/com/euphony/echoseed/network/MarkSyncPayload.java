package com.euphony.echoseed.network;

import com.euphony.echoseed.EchoSeed;
import com.euphony.echoseed.rules.LiveMark;
import com.euphony.echoseed.rules.MarkLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Optional;

public record MarkSyncPayload(Optional<LiveMark> mark) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MarkSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(EchoSeed.id("mark_sync"));
    public static final StreamCodec<FriendlyByteBuf, MarkSyncPayload> STREAM_CODEC =
            CustomPacketPayload.codec(MarkSyncPayload::write, MarkSyncPayload::read);

    private static MarkSyncPayload read(FriendlyByteBuf input) {
        if (!input.readBoolean()) {
            return new MarkSyncPayload(Optional.empty());
        }
        return new MarkSyncPayload(Optional.of(new LiveMark(
                new MarkLocation(input.readUtf(), input.readDouble(), input.readDouble(), input.readDouble()),
                input.readVarLong()
        )));
    }

    private void write(FriendlyByteBuf output) {
        Optional<LiveMark> live = mark.filter(LiveMark::isLive);
        if (live.isEmpty()) {
            output.writeBoolean(false);
            return;
        }
        LiveMark value = live.get();
        output.writeBoolean(true);
        output.writeUtf(value.location().dimension());
        output.writeDouble(value.location().x());
        output.writeDouble(value.location().y());
        output.writeDouble(value.location().z());
        output.writeVarLong(value.remainingMillis());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
