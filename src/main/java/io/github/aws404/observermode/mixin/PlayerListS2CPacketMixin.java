package io.github.aws404.observermode.mixin;

import io.github.aws404.observermode.ObserverModeMod;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerListS2CPacket.class)
public class PlayerListS2CPacketMixin {

	/**
	 * @reason change the gamemode in the player list header to adventure mode
	 */
	@Redirect(method = "<init>(Lnet/minecraft/network/packet/s2c/play/PlayerListS2CPacket$Action;[Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;getGameMode()Lnet/minecraft/world/GameMode;"))
	private GameMode modifyGameMode(ServerPlayerInteractionManager manager) {
		return manager.getGameMode() == ObserverModeMod.OBSERVER_MODE ? GameMode.ADVENTURE : manager.getGameMode();
	}

}
