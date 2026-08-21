package dev.christine.betterexperience.experience;

import dev.christine.betterexperience.config.ExperienceConfig;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ExperienceMultiplierStore {
    private static final Map<UUID, Integer> MULTIPLIERS = new HashMap<>();

    private ExperienceMultiplierStore() {
    }

    public static void set(PlayerEntity player, int multiplier) {
        MULTIPLIERS.put(player.getUuid(), ExperienceConfig.clampMultiplier(multiplier));
    }

    public static void remove(PlayerEntity player) {
        MULTIPLIERS.remove(player.getUuid());
    }

    public static int scale(PlayerEntity player, int experience) {
        if (player.getWorld().isClient()) {
            return experience;
        }

        int multiplier = MULTIPLIERS.getOrDefault(
                player.getUuid(),
                ExperienceConfig.DEFAULT_MULTIPLIER
        );
        return ExperienceConfig.scalePositiveExperience(experience, multiplier);
    }
}
