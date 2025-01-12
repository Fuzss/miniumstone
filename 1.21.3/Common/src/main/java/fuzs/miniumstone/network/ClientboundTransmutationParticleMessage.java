package fuzs.miniumstone.network;

import fuzs.puzzleslib.api.network.v3.ClientMessageListener;
import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;

public record ClientboundTransmutationParticleMessage(BlockPos pos, boolean smoke) implements ClientboundMessage<ClientboundTransmutationParticleMessage> {

    @Override
    public ClientMessageListener<ClientboundTransmutationParticleMessage> getHandler() {
        return new ClientMessageListener<>() {

            @Override
            public void handle(ClientboundTransmutationParticleMessage message, Minecraft client, ClientPacketListener handler, LocalPlayer player, ClientLevel level) {
                if (message.smoke) {
                    for (int i = 0; i < 8; ++i) {
                        level.addParticle(ParticleTypes.LARGE_SMOKE, message.pos.getX() + level.random.nextDouble(), ClientboundTransmutationParticleMessage.this.pos.getY() + 1.2D, ClientboundTransmutationParticleMessage.this.pos.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
                    }
                } else {
                    level.addParticle(ParticleTypes.EXPLOSION, message.pos.getX() + 0.5D, message.pos.getY() + 0.5D, message.pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
                }
            }
        };
    }
}
