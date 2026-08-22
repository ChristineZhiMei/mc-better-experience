package dev.christine.betterexperience.client.screen;

import dev.christine.betterexperience.client.BetterExperienceClient;
import dev.christine.betterexperience.client.config.ExperienceConfigStore;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public final class BetterExperienceConfigScreen extends Screen {
    private static final int PANEL_WIDTH = 310;
    private static final double[] MULTIPLIER_PRESETS = {
            0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9,
            1.0, 2.0, 3.0, 5.0, 10.0, 20.0, 50.0, 100.0,
            200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0
    };

    private final Screen parent;
    private double multiplier;

    public BetterExperienceConfigScreen(Screen parent) {
        super(Text.translatable("screen.better_experience.config.title"));
        this.parent = parent;
        this.multiplier = ExperienceConfigStore.getInstance().getMultiplier();
    }

    @Override
    protected void init() {
        int left = (width - PANEL_WIDTH) / 2;
        int centerY = height / 2;

        addDrawableChild(new ExperienceMultiplierSlider(left, centerY - 10, PANEL_WIDTH, 20));
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.cancel"), button -> close())
                .dimensions(left, centerY + 26, 151, 20)
                .build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), button -> save())
                .dimensions(left + 159, centerY + 26, 151, 20)
                .build());
    }

    private void save() {
        ExperienceConfigStore configStore = ExperienceConfigStore.getInstance();
        configStore.setMultiplier(multiplier);
        configStore.save();
        BetterExperienceClient.syncMultiplier();
        close();
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int centerY = height / 2;
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, centerY - 54, 0xFFFFFF);
        context.drawCenteredTextWithShadow(
                textRenderer,
                Text.translatable("screen.better_experience.config.description"),
                width / 2,
                centerY - 34,
                0xA0A0A0
        );
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static int findClosestPresetIndex(double multiplier) {
        int closestIndex = 0;
        double closestDistance = Math.abs(multiplier - MULTIPLIER_PRESETS[0]);

        for (int index = 1; index < MULTIPLIER_PRESETS.length; index++) {
            double distance = Math.abs(multiplier - MULTIPLIER_PRESETS[index]);
            if (distance < closestDistance) {
                closestIndex = index;
                closestDistance = distance;
            }
        }

        return closestIndex;
    }

    private static double presetIndexToSliderValue(int index) {
        return (double) index / (MULTIPLIER_PRESETS.length - 1);
    }

    private static int sliderValueToPresetIndex(double value) {
        return (int) Math.round(value * (MULTIPLIER_PRESETS.length - 1));
    }

    private static String formatMultiplier(double multiplier) {
        if (multiplier == Math.rint(multiplier)) {
            return Integer.toString((int) multiplier);
        }
        return String.format(java.util.Locale.ROOT, "%.1f", multiplier);
    }

    private final class ExperienceMultiplierSlider extends SliderWidget {
        private ExperienceMultiplierSlider(int x, int y, int width, int height) {
            super(
                    x,
                    y,
                    width,
                    height,
                    Text.empty(),
                    presetIndexToSliderValue(findClosestPresetIndex(multiplier))
            );
            multiplier = MULTIPLIER_PRESETS[sliderValueToPresetIndex(value)];
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Text.translatable(
                    "screen.better_experience.config.experience_multiplier",
                    formatMultiplier(multiplier)
            ));
        }

        @Override
        protected void applyValue() {
            int presetIndex = sliderValueToPresetIndex(value);
            value = presetIndexToSliderValue(presetIndex);
            multiplier = MULTIPLIER_PRESETS[presetIndex];
            updateMessage();
        }
    }
}
