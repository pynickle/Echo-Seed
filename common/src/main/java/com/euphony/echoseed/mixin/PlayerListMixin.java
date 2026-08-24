package com.euphony.echoseed.mixin;

import com.euphony.echoseed.mark.EchoMarks;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void echoSeed$syncMark(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        EchoMarks.sync(player);
    }

    @Inject(method = "remove", at = @At("TAIL"))
    private void echoSeed$forgetMark(ServerPlayer player, CallbackInfo ci) {
        EchoMarks.forget(player);
    }
}
