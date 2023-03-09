package fr.delta.minigamespec.mixin;

import fr.delta.minigamespec.MiniGameSpec;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameMode.class)
public class GameModeMixin {

    /**
     * @reason Adds the logic for observer mode abilities
     */
    @Inject(method = "setAbilities", at = @At("HEAD"), cancellable = true)
    private void setAbilities(PlayerAbilities abilities, CallbackInfo ci) {
        if (this.asGameMode() == MiniGameSpec.OBSERVER_MODE || this.asGameMode() == MiniGameSpec.ADVENTURE_SPEC_MOD) {
            abilities.allowFlying = true;
            abilities.flying = true;
            abilities.creativeMode = false;
            abilities.invulnerable = true;
            abilities.allowModifyWorld = false;
            ci.cancel();
        }
    }

    /**
     * @reason Sets block breaking to restricted in observer mode
     */
    @Inject(method = "isBlockBreakingRestricted", at = @At("HEAD"), cancellable = true)
    private void isBlockBreadingRestricted(CallbackInfoReturnable<Boolean> cir) {
        if (this.asGameMode() == MiniGameSpec.OBSERVER_MODE || this.asGameMode() == MiniGameSpec.ADVENTURE_SPEC_MOD) {
            cir.setReturnValue(true);
        }
    }

    private GameMode asGameMode() {
        return (GameMode)(Object)this;
    }
}
