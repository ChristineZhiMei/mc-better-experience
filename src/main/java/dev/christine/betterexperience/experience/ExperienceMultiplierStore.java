package dev.christine.betterexperience.experience;

import dev.christine.betterexperience.config.ExperienceConfig;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ExperienceMultiplierStore {
    private static final double ROUNDING_TOLERANCE = 1.0E-9;
    private static final Map<UUID, Double> MULTIPLIERS = new HashMap<>();
    private static final Map<UUID, Double> FRACTIONAL_EXPERIENCE = new HashMap<>();

    private ExperienceMultiplierStore() {
    }

    public static void set(PlayerEntity player, double multiplier) {
        UUID playerId = player.getUuid();
        MULTIPLIERS.put(playerId, ExperienceConfig.clampMultiplier(multiplier));
        FRACTIONAL_EXPERIENCE.remove(playerId);
    }

    public static void remove(PlayerEntity player) {
        UUID playerId = player.getUuid();
        MULTIPLIERS.remove(playerId);
        FRACTIONAL_EXPERIENCE.remove(playerId);
    }

    public static int scale(PlayerEntity player, int experience) {
        if (player.getWorld().isClient()) {
            return experience;
        }

        UUID playerId = player.getUuid();
        double multiplier = MULTIPLIERS.getOrDefault(
                playerId,
                ExperienceConfig.DEFAULT_MULTIPLIER
        );
        if (experience <= 0) {
            return experience;
        }

        double scaledExperience = ExperienceConfig.scalePositiveExperience(experience, multiplier)
                + FRACTIONAL_EXPERIENCE.getOrDefault(playerId, 0.0);
        if (scaledExperience >= Integer.MAX_VALUE) {
            FRACTIONAL_EXPERIENCE.remove(playerId);
            return Integer.MAX_VALUE;
        }

        int wholeExperience = (int) Math.floor(scaledExperience + ROUNDING_TOLERANCE);
        double remainder = Math.max(0.0, scaledExperience - wholeExperience);
        if (remainder < ROUNDING_TOLERANCE) {
            FRACTIONAL_EXPERIENCE.remove(playerId);
        } else {
            FRACTIONAL_EXPERIENCE.put(playerId, remainder);
        }
        return wholeExperience;
    }
}
