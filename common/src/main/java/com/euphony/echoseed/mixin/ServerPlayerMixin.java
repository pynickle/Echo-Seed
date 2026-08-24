package com.euphony.echoseed.mixin;

import com.euphony.echoseed.mark.EchoMarks;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void echoSeed$saveMark(ValueOutput output, CallbackInfo ci) {
        EchoMarks.save((ServerPlayer) (Object) this, output);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void echoSeed$loadMark(ValueInput input, CallbackInfo ci) {
        EchoMarks.load((ServerPlayer) (Object) this, input);
    }

    @Inject(method = "doTick", at = @At("TAIL"))
    private void echoSeed$tickMark(CallbackInfo ci) {
        EchoMarks.tick((ServerPlayer) (Object) this);
    }
}
