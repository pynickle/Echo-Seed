package com.euphony.echoseed.mixin.client;

import com.euphony.echoseed.client.EchoMarkClient;
import net.minecraft.client.Minecraft;
import net.minecraft.gizmos.Gizmos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "collectPerTickGizmos", at = @At("RETURN"))
    private void echoSeed$emitMark(CallbackInfoReturnable<Gizmos.TemporaryCollection> cir) {
        EchoMarkClient.emitGizmos();
    }
}
