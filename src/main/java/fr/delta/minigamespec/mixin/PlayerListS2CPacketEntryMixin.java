package fr.delta.minigamespec.mixin;

import fr.delta.minigamespec.MiniGameSpec;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(targets = "net/minecraft/network/packet/s2c/play/PlayerListS2CPacket$Entry")
public class PlayerListS2CPacketEntryMixin {

	/**
	 * @reason change the gamemode in the player list header to adventure mode
	 */
	@ModifyVariable(method = "<init>(Ljava/util/UUID;Lcom/mojang/authlib/GameProfile;ZILnet/minecraft/world/GameMode;Lnet/minecraft/text/Text;Lnet/minecraft/network/encryption/PublicPlayerSession$Serialized;)V", at = @At(value = "HEAD"), argsOnly = true)
	private static GameMode modifyGameMode(GameMode gameMode) {
		if(gameMode == MiniGameSpec.OBSERVER_MODE)
			return GameMode.ADVENTURE; //client hack to create the "observer mod"
		if(gameMode == MiniGameSpec.ADVENTURE_SPEC_MOD)
			return GameMode.ADVENTURE;
		return gameMode;
	}

}
