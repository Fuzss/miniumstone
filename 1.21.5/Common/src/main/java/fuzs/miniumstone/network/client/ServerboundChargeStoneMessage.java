package fuzs.miniumstone.network.client;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record ServerboundChargeStoneMessage(int carriedItemStack,
                                            InteractionHand interactionHand,
                                            boolean increaseCharge) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundChargeStoneMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.SHORT.map(Short::intValue, Integer::shortValue),
            ServerboundChargeStoneMessage::carriedItemStack,
            ExtraStreamCodecs.fromEnum(InteractionHand.class),
            ServerboundChargeStoneMessage::interactionHand,
            ByteBufCodecs.BOOL,
            ServerboundChargeStoneMessage::increaseCharge,
            ServerboundChargeStoneMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                context.packetListener()
                        .handleSetCarriedItem(new ServerboundSetCarriedItemPacket(ServerboundChargeStoneMessage.this.carriedItemStack));
                ItemStack itemInHand = context.player()
                        .getItemInHand(ServerboundChargeStoneMessage.this.interactionHand);
                if (itemInHand.is(ModRegistry.MINIUM_STONE_ITEM.value())) {
                    if (ServerboundChargeStoneMessage.this.increaseCharge) {
                        MiniumStoneItem.increaseCharge(itemInHand);
                    } else {
                        MiniumStoneItem.decreaseCharge(itemInHand);
                    }
                }
            }
        };
    }
}
