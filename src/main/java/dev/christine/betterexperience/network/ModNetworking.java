package dev.christine.betterexperience.network;

import dev.christine.betterexperience.experience.ExperienceMultiplierStore;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class ModNetworking {
    private ModNetworking() {
    }

    public static void initialize() {
        PayloadTypeRegistry.playC2S().register(
                UpdateExperienceMultiplierPayload.ID,
                UpdateExperienceMultiplierPayload.CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(
                UpdateExperienceMultiplierPayload.ID,
                (payload, context) -> ExperienceMultiplierStore.set(
                        context.player(),
                        payload.multiplier()
                )
        );
        ServerPlayConnectionEvents.DISCONNECT.register(
                (handler, server) -> ExperienceMultiplierStore.remove(handler.player)
        );
    }
}
