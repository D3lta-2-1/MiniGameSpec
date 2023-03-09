package io.github.aws404.observermode.mixin;
import io.github.aws404.observermode.ObserverModeMod;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameJoinS2CPacket.class)
public class GameJoinS2CPacketMixin {

    /**
     * @reason change the gamemode sent to the client to spectator mode
     */
    @ModifyVariable(method = "<init>(IZLnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;Ljava/util/Set;Lnet/minecraft/registry/DynamicRegistryManager$Immutable;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/RegistryKey;JIIIZZZZLjava/util/Optional;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static GameMode modifyGameMode(@Nullable GameMode gameMode) {
       return gameMode == ObserverModeMod.OBSERVER_MODE ? GameMode.SPECTATOR : gameMode;
    }
}
