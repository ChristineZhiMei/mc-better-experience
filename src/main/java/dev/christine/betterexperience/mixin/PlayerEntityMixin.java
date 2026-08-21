package dev.christine.betterexperience.mixin;

import dev.christine.betterexperience.experience.ExperienceMultiplierStore;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin {
    @ModifyVariable(method = "addExperience", at = @At("HEAD"), argsOnly = true)
    private int betterExperience$applyMultiplier(int experience) {
        return ExperienceMultiplierStore.scale((PlayerEntity) (Object) this, experience);
    }
}
