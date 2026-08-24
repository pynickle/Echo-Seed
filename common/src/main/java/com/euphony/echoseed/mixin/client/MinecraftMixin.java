package com.euphony.echoseed.mixin.client;

import com.euphony.echoseed.client.EchoMarkClient;
import com.euphony.echoseed.config.EchoConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "collectPerTickGizmos", at = @At("RETURN"))
    private void echoSeed$emitMark(CallbackInfoReturnable<Gizmos.TemporaryCollection> cir) {
        EchoMarkClient.emitGizmos();
    }

    @Inject(method = "disconnectFromWorld", at = @At("HEAD"))
    private void echoSeed$reloadLocalConfig(Component message, CallbackInfo ci) {
        EchoConfigs.reloadLocal();
    }
}
