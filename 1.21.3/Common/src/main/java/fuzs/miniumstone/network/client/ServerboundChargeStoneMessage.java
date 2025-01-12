package fuzs.miniumstone.network.client;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.puzzleslib.api.network.v3.ServerMessageListener;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record ServerboundChargeStoneMessage(int carriedItem, InteractionHand interactionHand, boolean increaseCharge) implements ServerboundMessage<ServerboundChargeStoneMessage> {

    @Override
    public ServerMessageListener<ServerboundChargeStoneMessage> getHandler() {
        return new ServerMessageListener<>() {

            @Override
            public void handle(ServerboundChargeStoneMessage message, MinecraftServer server, ServerGamePacketListenerImpl handler, ServerPlayer player, ServerLevel level) {
                handler.handleSetCarriedItem(new ServerboundSetCarriedItemPacket(message.carriedItem));
                ItemStack itemInHand = player.getItemInHand(message.interactionHand);
                if (itemInHand.is(ModRegistry.MINIUM_STONE_ITEM.value())) {
                    if (message.increaseCharge) {
                        MiniumStoneItem.increaseCharge(itemInHand);
                    } else {
                        MiniumStoneItem.decreaseCharge(itemInHand);
                    }
                }
            }
        };
    }
}
