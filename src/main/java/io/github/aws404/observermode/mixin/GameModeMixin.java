package io.github.aws404.observermode.mixin;

import io.github.aws404.observermode.ObserverModeConfig;
import io.github.aws404.observermode.ObserverModeMod;
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
        if (this.isObserver()) {
            abilities.allowFlying = ObserverModeConfig.MainConfig.canFly.getValue();
            abilities.flying = ObserverModeConfig.MainConfig.canFly.getValue();
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
        if (this.isObserver()) {
            cir.setReturnValue(true);
        }
    }

    private boolean isObserver() {
        return ((GameMode) (Object) this) == ObserverModeMod.OBSERVER_MODE;
    }
}
