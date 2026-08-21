package dev.christine.betterexperience.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.christine.betterexperience.BetterExperience;
import dev.christine.betterexperience.config.ExperienceConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExperienceConfigStore {
    private static final ExperienceConfigStore INSTANCE = new ExperienceConfigStore();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String MULTIPLIER_KEY = "experienceMultiplier";

    private final Path configPath = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("better-experience.json");
    private int multiplier = ExperienceConfig.DEFAULT_MULTIPLIER;

    private ExperienceConfigStore() {
    }

    public static ExperienceConfigStore getInstance() {
        return INSTANCE;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = ExperienceConfig.clampMultiplier(multiplier);
    }

    public void load() {
        multiplier = ExperienceConfig.DEFAULT_MULTIPLIER;
        if (!Files.isRegularFile(configPath)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(configPath)) {
            JsonObject config = JsonParser.parseReader(reader).getAsJsonObject();
            if (config.has(MULTIPLIER_KEY)) {
                setMultiplier(config.get(MULTIPLIER_KEY).getAsInt());
            }
        } catch (IOException | RuntimeException error) {
            BetterExperience.LOGGER.warn(
                    "Failed to load config {}, using defaults",
                    configPath,
                    error
            );
        }
    }

    public void save() {
        JsonObject config = new JsonObject();
        config.addProperty(MULTIPLIER_KEY, multiplier);

        try {
            Files.createDirectories(configPath.getParent());
            try (Writer writer = Files.newBufferedWriter(configPath)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException error) {
            BetterExperience.LOGGER.error("Failed to save config {}", configPath, error);
        }
    }
}
