package fr.delta.minigamespec.mixin;

import fr.delta.minigamespec.MiniGameSpec;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerRespawnS2CPacket.class)
public class PlayerRespawnS2CPacketMixin {

    /**
     * @reason change the gamemode sent to the client to spectator mode
     */
    @ModifyVariable(method = "<init>(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/RegistryKey;JLnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;ZZBLjava/util/Optional;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static GameMode modifyGameMode(GameMode gameMode) {
        if(gameMode == MiniGameSpec.OBSERVER_MODE)
            return GameMode.SPECTATOR;
        if(gameMode == MiniGameSpec.ADVENTURE_SPEC_MODE)
            return GameMode.ADVENTURE;
        return gameMode;
    }

}
