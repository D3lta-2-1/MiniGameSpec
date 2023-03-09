package fr.delta.minigamespec.mixin;

import fr.delta.minigamespec.MiniGameSpec;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(GameStateChangeS2CPacket.class)
public class GameStateChangeS2CPacketMixin {

    /**
     * @reason Changes the gamemode sent to the client to spectator mode
     */
    @ModifyVariable(method = "<init>(Lnet/minecraft/network/packet/s2c/play/GameStateChangeS2CPacket$Reason;F)V", at = @At("HEAD"), argsOnly = true)
    private static float init(float value, GameStateChangeS2CPacket.Reason reason) {
        if (reason == GameStateChangeS2CPacket.GAME_MODE_CHANGED) {
            if (value == MiniGameSpec.OBSERVER_MODE.getId())
                return GameMode.SPECTATOR.getId();
            if (value == MiniGameSpec.ADVENTURE_SPEC_MODE.getId())
                return GameMode.ADVENTURE.getId();
        }
        return value;
    }
}
