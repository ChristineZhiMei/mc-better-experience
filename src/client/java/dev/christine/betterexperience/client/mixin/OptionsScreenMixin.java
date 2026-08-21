package dev.christine.betterexperience.client.mixin;

import dev.christine.betterexperience.client.screen.BetterExperienceConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OptionsScreen.class)
abstract class OptionsScreenMixin {
    @Redirect(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ThreePartsLayoutWidget;addBody(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;"
            )
    )
    private Widget betterExperience$addConfigEntry(
            ThreePartsLayoutWidget layout,
            Widget body
    ) {
        if (body instanceof GridWidget grid) {
            ButtonWidget configButton = ButtonWidget.builder(
                    Text.translatable("menu.better_experience.config"),
                    button -> MinecraftClient.getInstance().setScreen(
                            new BetterExperienceConfigScreen((Screen) (Object) this)
                    )
            ).width(310).build();
            grid.add(configButton, 5, 0, 1, 2);
        }

        return layout.addBody(body);
    }
}
