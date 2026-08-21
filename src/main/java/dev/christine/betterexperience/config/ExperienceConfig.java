package dev.christine.betterexperience.config;

public final class ExperienceConfig {
    public static final int MIN_MULTIPLIER = 0;
    public static final int MAX_MULTIPLIER = 100;
    public static final int DEFAULT_MULTIPLIER = 1;

    private ExperienceConfig() {
    }

    public static int clampMultiplier(int multiplier) {
        return Math.clamp(multiplier, MIN_MULTIPLIER, MAX_MULTIPLIER);
    }

    public static int scalePositiveExperience(int experience, int multiplier) {
        if (experience <= 0) {
            return experience;
        }

        long scaledExperience = (long) experience * clampMultiplier(multiplier);
        return (int) Math.min(scaledExperience, Integer.MAX_VALUE);
    }
}
