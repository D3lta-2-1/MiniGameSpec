package io.github.aws404.observermode.mixin;

import io.github.aws404.observermode.ObserverModeMod;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRespawnS2CPacket.class)
public class PlayerRespawnS2CPacketMixin {

    @Shadow private GameMode gameMode;

    /**
     * @reason change the gamemode sent to the client to spectator mode
     */
    @Inject(method = "<init>(Lnet/minecraft/world/dimension/DimensionType;Lnet/minecraft/util/registry/RegistryKey;JLnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;ZZZ)V", at = @At("TAIL"))
    private void modifyGameMode(DimensionType dimensionType, RegistryKey<World> registryKey, long l, GameMode gameMode, GameMode previousGameMode, boolean bl, boolean bl2, boolean bl3, CallbackInfo ci) {
        this.gameMode = gameMode == ObserverModeMod.OBSERVER_MODE ? GameMode.SPECTATOR : gameMode;
    }

}
