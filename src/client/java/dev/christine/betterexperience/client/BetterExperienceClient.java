package dev.christine.betterexperience.client;

import dev.christine.betterexperience.client.config.ExperienceConfigStore;
import dev.christine.betterexperience.network.UpdateExperienceMultiplierPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class BetterExperienceClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExperienceConfigStore.getInstance().load();
        ClientPlayConnectionEvents.JOIN.register(
                (handler, sender, client) -> syncMultiplier()
        );
    }

    public static void syncMultiplier() {
        if (!ClientPlayNetworking.canSend(UpdateExperienceMultiplierPayload.ID)) {
            return;
        }

        int multiplier = ExperienceConfigStore.getInstance().getMultiplier();
        ClientPlayNetworking.send(new UpdateExperienceMultiplierPayload(multiplier));
    }
}
