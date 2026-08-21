package dev.christine.betterexperience.network;

import dev.christine.betterexperience.BetterExperience;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record UpdateExperienceMultiplierPayload(double multiplier) implements CustomPayload {
    public static final Id<UpdateExperienceMultiplierPayload> ID =
            new Id<>(BetterExperience.id("update_experience_multiplier"));
    public static final PacketCodec<RegistryByteBuf, UpdateExperienceMultiplierPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.DOUBLE,
                    UpdateExperienceMultiplierPayload::multiplier,
                    UpdateExperienceMultiplierPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
