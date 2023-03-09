package io.github.aws404.observermode.mixin;

import io.github.aws404.observermode.ObserverModeMod;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(GameJoinS2CPacket.class)
public class GameJoinS2CPacketMixin {

    @Shadow private GameMode gameMode;

    /**
     * @reason change the gamemode sent to the client to spectator mode
     */
    @Inject(method = "<init>(ILnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;JZLjava/util/Set;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Lnet/minecraft/world/dimension/DimensionType;Lnet/minecraft/util/registry/RegistryKey;IIZZZZ)V", at = @At("TAIL"))
    private void modifyGameMode(int playerEntityId, GameMode gameMode, GameMode previousGameMode, long sha256Seed, boolean hardcore, Set<RegistryKey<World>> dimensionIds, DynamicRegistryManager.Impl registryManager, DimensionType dimensionType, RegistryKey<World> dimensionId, int maxPlayers, int chunkLoadDistance, boolean reducedDebugInfo, boolean showDeathScreen, boolean debugWorld, boolean flatWorld, CallbackInfo ci) {
        this.gameMode = gameMode == ObserverModeMod.OBSERVER_MODE ? GameMode.SPECTATOR : gameMode;
    }
}
