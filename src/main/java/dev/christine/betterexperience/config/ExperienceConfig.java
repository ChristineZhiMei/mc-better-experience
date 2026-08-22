package dev.christine.betterexperience.config;

public final class ExperienceConfig {
    public static final double MIN_MULTIPLIER = 0.0;
    public static final double MAX_MULTIPLIER = 1000.0;
    public static final double DEFAULT_MULTIPLIER = 1.0;

    private ExperienceConfig() {
    }

    public static double clampMultiplier(double multiplier) {
        if (!Double.isFinite(multiplier)) {
            return DEFAULT_MULTIPLIER;
        }
        return Math.clamp(multiplier, MIN_MULTIPLIER, MAX_MULTIPLIER);
    }

    public static double scalePositiveExperience(int experience, double multiplier) {
        if (experience <= 0) {
            return experience;
        }

        return experience * clampMultiplier(multiplier);
    }
}
