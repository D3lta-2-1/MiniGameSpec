package fr.delta.minigamespec.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import fr.delta.minigamespec.MiniGameSpec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nucleoid.plasmid.game.manager.GameSpaceManager;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow @Final
    public ServerPlayerInteractionManager interactionManager;
    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    @Shadow public abstract World getWorld();

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) { super(world, pos, yaw, profile); }

    /**
     * @reason Provides the logic for setting the player into observer mode
     */
    @Inject(method = "changeGameMode", at = @At("HEAD"), cancellable = true)
    private void setGameModeHead(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (gameMode == MiniGameSpec.OBSERVER_MODE) {
            this.interactionManager.changeGameMode(gameMode);

            this.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.GAME_MODE_CHANGED, (float)GameMode.SPECTATOR.getId()));
            this.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_GAME_MODE, (ServerPlayerEntity) (Object) this));
            this.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, (ServerPlayerEntity) (Object) this));

            this.dropShoulderEntities();
            this.stopRiding();
            this.sendAbilitiesUpdate();
            this.markEffectsDirty();

            cir.setReturnValue(true);
        }
        if (gameMode == MiniGameSpec.ADVENTURE_SPEC_MODE) {
            this.interactionManager.changeGameMode(gameMode);

            this.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.GAME_MODE_CHANGED, (float)GameMode.ADVENTURE.getId()));
            this.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_GAME_MODE, (ServerPlayerEntity) (Object) this));
            this.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, (ServerPlayerEntity) (Object) this));

            this.dropShoulderEntities();
            this.stopRiding();
            this.sendAbilitiesUpdate();
            this.markEffectsDirty();

            cir.setReturnValue(true);
        }
    }

    /**
     * @reason Forces the player list name to update after the gamemode has been changed
     */
    @Inject(method = "changeGameMode", at = @At("TAIL"))
    private void setGameModeTail(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        this.networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, (ServerPlayerEntity) (Object) this));
    }

    /**
     * @reason Formats the player list name to be dark gray and italic
     */
    @Inject(method = "getPlayerListName", at = @At("HEAD"), cancellable = true)
    private void getPlayerListName(CallbackInfoReturnable<Text> cir) {
        if (interactionManager.getGameMode() == MiniGameSpec.OBSERVER_MODE || interactionManager.getGameMode() == MiniGameSpec.ADVENTURE_SPEC_MODE) {
            cir.setReturnValue(((MutableText) getName()).formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
        }
    }

    /**
     * @reason Tricks many parts of the player logic to act as if player is in specator mode
     * For example:
     * * {@link ServerPlayNetworkHandler#onPlayerAction(PlayerActionC2SPacket)}
     * * {@link ServerPlayNetworkHandler#onClickSlot(ClickSlotC2SPacket)}
     * * {@link ServerPlayNetworkHandler#onCraftRequest(CraftRequestC2SPacket)}
     * * {@link ServerPlayNetworkHandler#onButtonClick(ButtonClickC2SPacket)}
     * * @link ServerPlayNetworkHandler#onConfirmScreenAction(ConfirmScreenActionC2SPacket)}
     * * {@link ServerPlayerEntity#playerTick()}
     * * {@link ServerPlayerEntity#onDeath(DamageSource)}
     * * {@link ServerPlayerEntity#canBeSpectated(ServerPlayerEntity)}
     * * {@link ServerPlayerEntity#openHandledScreen(NamedScreenHandlerFactory)}
     * * {@link ServerPlayerEntity#copyFrom(ServerPlayerEntity, boolean)}
     */
    @Redirect(method = "isSpectator", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;getGameMode()Lnet/minecraft/world/GameMode;"))
    private GameMode isSpectatorGameModeRedirect(ServerPlayerInteractionManager manager) {
        var gameMode = manager.getGameMode();
        return gameMode == MiniGameSpec.OBSERVER_MODE || gameMode == MiniGameSpec.ADVENTURE_SPEC_MODE ? GameMode.SPECTATOR : gameMode;
    }

    /**
     * @reason Manages the player and mob spectating logics
     */
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void attack(Entity target, CallbackInfo ci) {
        var gameSpace = GameSpaceManager.get().byWorld(world);
        if(gameSpace == null) return;
        if (gameSpace.getBehavior().testRule(MiniGameSpec.OBSERVER_CAN_HIT) == ActionResult.FAIL && interactionManager.getGameMode() == MiniGameSpec.OBSERVER_MODE) {
            ci.cancel();
        }
        else if (gameSpace.getBehavior().testRule(MiniGameSpec.ADVENTURE_SPEC_CAN_HIT) == ActionResult.FAIL && interactionManager.getGameMode() == MiniGameSpec.ADVENTURE_SPEC_MODE) {
            ci.cancel();
        }
    }

    /**
     * @reason Stops any player in observer mode from sleeping
     */
    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    private void trySleep(BlockPos pos, CallbackInfoReturnable<Either<SleepFailureReason, Unit>> cir) {
        if (interactionManager.getGameMode() == MiniGameSpec.OBSERVER_MODE && interactionManager.getGameMode() == MiniGameSpec.ADVENTURE_SPEC_MODE) {
            cir.setReturnValue(Either.left(SleepFailureReason.OTHER_PROBLEM));
        }
    }
}
