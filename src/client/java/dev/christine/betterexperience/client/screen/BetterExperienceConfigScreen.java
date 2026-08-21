package dev.christine.betterexperience.client.screen;

import dev.christine.betterexperience.client.BetterExperienceClient;
import dev.christine.betterexperience.client.config.ExperienceConfigStore;
import dev.christine.betterexperience.config.ExperienceConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public final class BetterExperienceConfigScreen extends Screen {
    private static final int PANEL_WIDTH = 310;

    private final Screen parent;
    private int multiplier;

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

    private final class ExperienceMultiplierSlider extends SliderWidget {
        private ExperienceMultiplierSlider(int x, int y, int width, int height) {
            super(
                    x,
                    y,
                    width,
                    height,
                    Text.empty(),
                    (double) multiplier / ExperienceConfig.MAX_MULTIPLIER
            );
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Text.translatable(
                    "screen.better_experience.config.experience_multiplier",
                    multiplier
            ));
        }

        @Override
        protected void applyValue() {
            multiplier = (int) Math.round(value * ExperienceConfig.MAX_MULTIPLIER);
            updateMessage();
        }
    }
}
