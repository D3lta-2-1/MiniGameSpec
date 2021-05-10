package io.github.aws404.observermode.mixin;

import io.github.aws404.observermode.ObserverModeMod;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameStateChangeS2CPacket.class)
public class GameStateChangeS2CPacketMixin {

    @Shadow private float value;

    /**
     * @reason Changes the gamemode sent to the client to spectator mode
     */
    @Inject(method = "<init>(Lnet/minecraft/network/packet/s2c/play/GameStateChangeS2CPacket$Reason;F)V", at = @At("TAIL"))
    private void init(GameStateChangeS2CPacket.Reason reason, float value, CallbackInfo ci) {
        if (reason == GameStateChangeS2CPacket.GAME_MODE_CHANGED) {
            if (value == ObserverModeMod.OBSERVER_MODE.getId()) {
                this.value = GameMode.SPECTATOR.getId();
            }
        }
    }
}
