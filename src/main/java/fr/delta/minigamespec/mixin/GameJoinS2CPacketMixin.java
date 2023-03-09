package fr.delta.minigamespec.mixin;
import fr.delta.minigamespec.MiniGameSpec;
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
        if(gameMode == MiniGameSpec.OBSERVER_MODE)
            return GameMode.SPECTATOR;
        if(gameMode == MiniGameSpec.ADVENTURE_SPEC_MOD)
            return GameMode.ADVENTURE;
        return gameMode;
    }
}
